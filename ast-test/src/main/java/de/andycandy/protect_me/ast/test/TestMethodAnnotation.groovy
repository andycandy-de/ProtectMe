package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

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
