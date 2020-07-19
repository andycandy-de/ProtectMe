package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestGenerationMethodWithMultipleInterfacesTest {

	@Test
	void setMethodGenerationWithMultipleInterfaces() {
		
		TestGenerationMethodWithMultipleInterfaces testObject = new TestGenerationMethodWithMultipleInterfaces()
		testObject.anyValue = 'test'
		
		def readOnlyObject = testObject.toProtectedReadOnly()
		def writeOnlyObject = testObject.toProtectedWriteOnly()
		
		assertThat(testObject.anyValue).isEqualTo('test')
		assertThat(readOnlyObject.anyValue).isEqualTo(testObject.anyValue)
		assertThatThrownBy( { readOnlyObject.anyValue = 'tryChange' } ).isInstanceOf(ReadOnlyPropertyException)
		assertThatThrownBy( { writeOnlyObject.anyValue } ).isInstanceOf(MissingPropertyException)

		writeOnlyObject.anyValue = 'changed'
		assertThat(testObject.anyValue).isEqualTo('changed')
		assertThat(readOnlyObject.anyValue).isEqualTo(testObject.anyValue)
	}
	
}
