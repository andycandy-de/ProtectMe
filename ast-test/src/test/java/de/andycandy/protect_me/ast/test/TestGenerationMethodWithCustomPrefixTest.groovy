package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat

import de.andycandy.protect_me.ast.test.TestGenerationMethodWithCustomPrefix.ReadOnly
import org.junit.jupiter.api.Test

class TestGenerationMethodWithCustomPrefixTest {

	@Test
	void testAutoMethodCreation() {

		TestGenerationMethodWithCustomPrefix testObject = new TestGenerationMethodWithCustomPrefix()

		ReadOnly readOnly = testObject.protectReadOnly()

		assertThat(readOnly).isInstanceOf(ReadOnly)
		assertThat(readOnly).isNotInstanceOf(TestGenerationMethodWithCustomPrefix)
	}
}
