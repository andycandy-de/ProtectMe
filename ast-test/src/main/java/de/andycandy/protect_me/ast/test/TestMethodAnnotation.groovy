package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 * 
 * Simple test class to test the {@link Protect} annotation on method level.
 * The annotated method will return a protecting delegating proxy which
 * implements the return type of the method
 * 
 * @author Andreas
 *
 */

class TestMethodAnnotation {
	
	List<String> list = []
	
	@Protect
	Iterable getProtectedIterable() {
		list
	}
	
	Iterable getIterable() {
		list
	}
	
}