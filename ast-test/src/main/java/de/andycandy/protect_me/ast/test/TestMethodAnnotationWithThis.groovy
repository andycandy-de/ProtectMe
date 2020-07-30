package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 *
 * Simple test class to test the {@link Protect} annotation on method level.
 * This test class is created to test the method returning a protecting delegating
 * proxy of <code>this</code> {@link TestMethodAnnotation}
 *
 * @author Andreas
 *
 */

class TestMethodAnnotationWithThis implements ReadOnly {
	
	String value
	
	@Protect
	ReadOnly getReadOnly() {
		return this
	}
	
	interface ReadOnly {
		String getValue()
	}
}
