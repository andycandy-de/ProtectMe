package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat

import de.andycandy.protect_me.ast.test.TestGenerationMethodWithCustomPrefix.ReadOnly
import org.junit.jupiter.api.Test

class TestGenerationMethodWithCustomPrefixTest {

	@Test
	void testAutoMethodCreation() {

		TestGenerationMethodWithCustomPrefix testGenerationMethodWithCustomPrefix = new TestGenerationMethodWithCustomPrefix()

		ReadOnly readOnly = testGenerationMethodWithCustomPrefix.protectReadOnly()

		assertThat(readOnly).isInstanceOf(ReadOnly)
		assertThat(readOnly).isNotInstanceOf(TestGenerationMethodWithCustomPrefix)
	}
}
