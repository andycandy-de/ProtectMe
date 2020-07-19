package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

@Protect(classes = [ReadOnly.class], methodPrefix = "protect")
class TestGenerationMethodWithCustomPrefix {
	
	String worthProtecting

	interface ReadOnly {
		
		String getWorthProtecting()
		
	}
		
}