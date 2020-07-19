package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

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
