package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 * 
 * Simple test class to test the {@link Protect} annotation on method level.
 * This test class is created to test the annotation on multiple methods {@link TestMethodAnnotation}
 * 
 * @author Andreas
 *
 */

class TestMethodAnnotationWithMultipleMehtods implements ReadOnly {
	
	List<String> list = []
	
	String value
	
	@Protect
	ReadOnly getReadOnly() {
		return this
	}
	
	@Protect
	Iterable<String> getIterable() {
		return list
	}
	
	interface ReadOnly {
		String getValue()
	}
}
