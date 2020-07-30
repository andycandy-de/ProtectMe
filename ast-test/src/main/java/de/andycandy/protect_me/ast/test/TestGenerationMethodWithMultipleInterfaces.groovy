package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 * 
 * Simple test class to test the {@link Protect} annotation on class level.
 * This annotation defines multiple interfaces and should generate a method
 * for each interface {@link TestGenerationMethod}
 * 
 * @author Andreas
 *
 */

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