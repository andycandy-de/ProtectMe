package de.andycandy.protect_me.ast;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.classgen.VariableScopeVisitor;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import de.andycandy.protect_me.util.ProtectUtil;
import groovyjarjarasm.asm.Opcodes;

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class ProtectASTTransformation implements ASTTransformation {

	public static final String DEFAULT_METHOD_PREFIX = "toProtected";
	
	public static final String VALID_METHOD_NAME_REGEX = "^[A-Za-z]\\w*$"; 

	@Override
	public void visit(ASTNode[] nodes, SourceUnit source) {

		Context context = new Context();
		context.nodes = nodes;
		context.source = source;
		context.astAnnotation = (AnnotationNode) nodes[0];

		ASTNode node = context.nodes[1];
		try {
			if (node instanceof ClassNode) {
				context.astClass = (ClassNode) node;
				astForClass(context);
			} else if (node instanceof MethodNode) {
				context.astMethod = (MethodNode) node;
				astForMethod(context);
			}
		} catch (Exception e) {
			addError(context, context.astClass, e.toString());
		}
	}

	private void astForMethod(Context context) {

		checkAnnotationForMethodAST(context);
		if (context.errors) {
			return;
		}

		if (context.astMethod.getReturnType().getName().equals("void")) {
			addError(context, context.astMethod, "Void methods cannot return a protected Object!");
			return;
		}

		addProtectionToMethodReturn(context);
	}

	private void addProtectionToMethodReturn(Context context) {
		
		BlockStatement blockStatement = (BlockStatement) context.astMethod.getCode();
		ClosureExpression closureExpression = new ClosureExpression(new Parameter[0], blockStatement);

		MethodCallExpression closureCall = new MethodCallExpression(closureExpression, "call",
				new ArgumentListExpression());

		ReturnStatement returnStatement = new ReturnStatement(createStaticCallExpression(closureCall, context.astMethod.getReturnType()));
		context.astMethod.setCode(new BlockStatement(
				Arrays.asList(returnStatement), new VariableScope()));
		
		VariableScopeVisitor scopeVisitor = new VariableScopeVisitor(context.source);
		scopeVisitor.visitClass(context.astMethod.getDeclaringClass()); // To fix the VariableScope
	}

	private void checkAnnotationForMethodAST(Context context) {

		if (context.astAnnotation.getMember("classes") != null) {
			addError(context, context.astMethod, "Classes are not allowed on method annotation!");
			context.errors = true;
		}

		if (context.astAnnotation.getMember("methodPrefix") != null) {
			addError(context, context.astMethod, "MethodPrefix is not allowed on method annotation!");
			context.errors = true;
		}
	}

	protected void astForClass(Context context) {

		checkAnnotationForClassAST(context);
		if (context.errors) {
			return;
		}

		context.protectClassExpression.forEach((classExpression) -> addMethodToClass(context, classExpression));
	}

	private void addMethodToClass(Context context, ClassExpression classExpression) {

		String methodName = context.methodPrefix + getShortName(classExpression.getType().getName());
		if (context.astClass.getMethod(methodName, new Parameter[0]) != null) {
			addError(context, context.astClass.getMethod(methodName, new Parameter[0]),
					"There is already a Method with the name: " + methodName);
			context.errors = true;
			return;
		}

		ClassNode currentClassNode = classExpression.getType();

		if (!context.astClass.declaresInterface(currentClassNode)) {
			context.astClass.addInterface(currentClassNode);
		}

		Statement statement = createStatement(currentClassNode);
		context.astClass.addMethod(methodName, Opcodes.ACC_PUBLIC, currentClassNode, new Parameter[0], new ClassNode[0],
				statement);
	}


	private void checkAnnotationForClassAST(Context context) {

		findProtectClassExpression(context);
		findMethodPrefix(context);
	}

	private void findMethodPrefix(Context context) {
		
		Expression methodPrefixExpression = context.astAnnotation.getMember("methodPrefix");
		
		if (methodPrefixExpression == null) {
			context.methodPrefix = DEFAULT_METHOD_PREFIX;
			return;
		}

		context.methodPrefix = getStringFromExpression(methodPrefixExpression);
		
		if (context.methodPrefix == null) {
			addError(context, context.astAnnotation, "methodPrefix must be a String!");
			context.errors = true;
		}
		else if (!context.methodPrefix.matches(VALID_METHOD_NAME_REGEX)) {
			addError(context, context.astAnnotation, "methodPrefix must be a valid method Name!");
			context.errors = true;
		}
	}

	
	private String getStringFromExpression(Expression expression) {
		
		if (!(expression instanceof ConstantExpression)) {
			return null;
		}
		
		ConstantExpression constantExpression = (ConstantExpression) expression;
		Object value = constantExpression.getValue();
		
		if (value instanceof String) {
			return (String) value;
		}
		
		return null;
	}
	
	private void findProtectClassExpression(Context context) {
		
		ListExpression listExpression = getListExpressionFromAnnotation(context);
		if (listExpression == null) {
			addError(context, context.astAnnotation, "No list with classes defined!");
			context.errors = true;
			return;
		}

		Set<Expression> notClassExpressions = listExpression.getExpressions().stream()
				.filter((e) -> !(e instanceof ClassExpression)).collect(Collectors.toSet());

		if (!notClassExpressions.isEmpty()) {
			addError(context, context.astAnnotation, "Annotation value must contain only classes!");
			context.errors = true;
		}

		context.protectClassExpression = listExpression.getExpressions().stream()
				.filter((e) -> e instanceof ClassExpression).map((e) -> (ClassExpression) e)
				.collect(Collectors.toSet());

		if (context.protectClassExpression.isEmpty()) {
			addError(context, context.astAnnotation, "Annotation value must contain at least one class!");
			context.errors = true;
		}
	}

	private ListExpression getListExpressionFromAnnotation(Context context) {
		Expression expression = context.astAnnotation.getMember("classes");
		if (expression instanceof ListExpression) {
			return (ListExpression) expression;
		}
		
		return null;
	}

	private Statement createStatement(ClassNode classNode) {

		VariableExpression thisExpression = new VariableExpression("this", ClassHelper.make(Object.class));
		ReturnStatement returnStatement = new ReturnStatement(createStaticCallExpression(thisExpression, classNode));

		return new BlockStatement(Arrays.asList(returnStatement), new VariableScope());
	}
	
	private Expression createStaticCallExpression(Expression toProtectExpression, ClassNode returnClassNode) {

		ArgumentListExpression arguments = new ArgumentListExpression(toProtectExpression,
				new ClassExpression(returnClassNode));
		StaticMethodCallExpression staticMethodCallExpression = new StaticMethodCallExpression(
				ClassHelper.make(ProtectUtil.class), "protect", arguments);
		
		return staticMethodCallExpression;
	}
	
	private void addError(Context context, ASTNode node, String msg) {
		int line = node.getLineNumber();
		int col = node.getColumnNumber();
		SyntaxException se = new SyntaxException(msg + '\n', line, col);
		Message message = new SyntaxErrorMessage(se, context.source);
		context.source.getErrorCollector().addErrorAndContinue(message);
	}

	protected String getShortName(String name) {

		int idx = Math.max(name.lastIndexOf('$'), name.lastIndexOf('.'));
		if (idx > 0) {
			return name.substring(idx + 1);
		}
		return name;
	}

	private class Context {

		private ASTNode[] nodes;

		private SourceUnit source;

		private AnnotationNode astAnnotation;

		private ClassNode astClass;

		private Set<ClassExpression> protectClassExpression;

		private String methodPrefix = DEFAULT_METHOD_PREFIX;

		private MethodNode astMethod;

		private boolean errors = false;

	}

}
