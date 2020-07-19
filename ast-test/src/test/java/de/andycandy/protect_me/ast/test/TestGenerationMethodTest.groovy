package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

import de.andycandy.protect_me.ast.test.TestGenerationMethod.ReadOnly

class TestGenerationMethodTest {

	@Test
	void testAutoMethodCreation() {
		
		TestGenerationMethod testObject = new TestGenerationMethod()
		
		ReadOnly readOnly = testObject.toProtectedReadOnly()
		
		assertThat(readOnly).isInstanceOf(ReadOnly)
		assertThat(readOnly).isNotInstanceOf(TestGenerationMethodTest)
	}
	
}
