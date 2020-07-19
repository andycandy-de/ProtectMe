package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

@Protect(classes = [ReadOnly.class, WriteOnly.class])
class TestGenerationMethodWithMultipleInterfaces {
	
	String anyValue

	interface ReadOnly {
		
		String getAnyValue()
		
	}
	
	interface WriteOnly {
		
		void setAnyValue(String anyValue)
		
	}
		
}