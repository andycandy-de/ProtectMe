package de.andycandy.protect_me.ast.test

import de.andycandy.protect_me.ast.Protect

/**
 * Simple test class to test the {@link Protect} annotation on class level.
 * This should generate a method 'toProtectedReadOnly' which can
 * be executed to get a protecting delegating proxy for the current instance.
 * 
 * @author Andreas
 *
 */

@Protect(classes = [ReadOnly.class])
class TestGenerationMethod {

	String worthProtecting

	interface ReadOnly {

		String getWorthProtecting()
	}
}