package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 * 
 * Simple test class to test the {@link Protect} annotation on class level.
 * This annotation defines the method prefix for the method generation {@link TestGenerationMethod}
 * 
 * @author Andreas
 *
 */

@Protect(classes = [ReadOnly.class], methodPrefix = "protect")
class TestGenerationMethodWithCustomPrefix {
	
	String worthProtecting

	interface ReadOnly {
		
		String getWorthProtecting()
		
	}
		
}