package de.andycandy.protect_me.ast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
@GroovyASTTransformationClass(classes = ProtectASTTransformation.class)
public @interface Protect {

	Class<?>[] classes() default {};
	
	String methodPrefix() default ProtectASTTransformation.DEFAULT_METHOD_PREFIX;
	
}
