package de.andycandy.protect_me.ast;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

/**
 * This ast annotation can be use in groovy on method or class level. When the
 * annotation is defined on method level the returning object is automatically
 * protected with a delegating proxy. On class level the ast transformation
 * generates a new method which generates an protecting delegating proxy.
 * 
 * <pre>
 * &#64;Protect(classes = [ReadOnly.class])
 * class TestGenerationMethod {
 *   String worthProtecting
 * 
 *   interface ReadOnly {
 *     String getWorthProtecting()
 *   }
 * }
 * </pre>
 * 
 * <pre>
 * class TestMethodAnnotation {
 *   List list = []
 * 
 *   &#64;Protect
 *   Iterable getProtectedIterable() {
 *     list
 *   }
 * }
 * </pre>
 * @author Andreas
 *
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.METHOD })
@GroovyASTTransformationClass(classes = ProtectASTTransformation.class)
public @interface Protect {

	Class<?>[] classes() default {};

	String methodPrefix() default ProtectASTTransformation.DEFAULT_METHOD_PREFIX;

}
