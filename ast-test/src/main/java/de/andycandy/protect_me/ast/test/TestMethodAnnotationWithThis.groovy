package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

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
