package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

@Protect(classes = [ReadOnly.class])
class TestGenerationMethod {
	
	String worthProtecting

	interface ReadOnly {
		
		String getWorthProtecting()
		
	}
		
}