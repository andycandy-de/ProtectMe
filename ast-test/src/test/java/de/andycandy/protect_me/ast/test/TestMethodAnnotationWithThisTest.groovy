package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatIllegalStateException
import static org.assertj.core.api.Assertions.assertThatThrownBy

import org.junit.jupiter.api.Test

import de.andycandy.protect_me.ast.test.TestMethodAnnotationWithThis.ReadOnly

class TestMethodAnnotationWithThisTest {

	@Test
	void testMethodAnnotationWithThis() {
		
		TestMethodAnnotationWithThis testObject = new TestMethodAnnotationWithThis()
		
		testObject.value = 'what'
		
		assertThat(testObject.getReadOnly()).isNotInstanceOf(TestMethodAnnotationWithThis).isInstanceOf(ReadOnly)
		assertThat(testObject.getReadOnly().value).isEqualTo('what')
		assertThatThrownBy({ testObject.getReadOnly().value = 'haha' }).isInstanceOf(ReadOnlyPropertyException)
		assertThat(testObject.getReadOnly().value).isEqualTo('what')
	}
	
}
