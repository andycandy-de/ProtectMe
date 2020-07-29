package de.andycandy.protect_me.ast.test;

import static org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test

class TestMethodAnnotationWithMultipleMehtodsTest {

	@Test
	void testMethodAnnotationWithMultipleMethods() {
		
		TestMethodAnnotationWithMultipleMehtods testObject = new TestMethodAnnotationWithMultipleMehtods()
		
		testObject.value = '123'
		testObject.list << '1' << '2' << '3'
		
		assertThat(testObject.readOnly.value).isEqualTo('123')
		assertThat(testObject.iterable).isInstanceOf(Iterable)
		
		def itList = []
		testObject.iterable.each { itList.add(it) }
		assertThat(itList).containsExactly('1', '2', '3')
	}
	
}
