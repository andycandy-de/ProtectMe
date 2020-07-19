package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

import org.junit.jupiter.api.Test

class TestMethodAnnotationTest {

	@Test
	void testProtectedMethodAnnotation() {
		
		TestMethodAnnotation testObject = new TestMethodAnnotation()
		
		testObject.list << '1' << '2'
		
		assertThat(testObject.list).isEqualTo(['1', '2'])
		
		assertThat(testObject.getIterable()).isInstanceOf(List)
		testObject.getIterable() << '3'
		assertThat(testObject.list).isEqualTo(['1', '2', '3'])
		
		assertThat(testObject.getProtectedIterable()).isNotInstanceOf(List)
		assertThatThrownBy( { (List)testObject.getProtectedIterable() } ).isInstanceOf(ClassCastException)
		assertThatThrownBy( { testObject.getProtectedIterable() << '4' } ).isInstanceOf(MissingMethodException)
		
	}
	
}
