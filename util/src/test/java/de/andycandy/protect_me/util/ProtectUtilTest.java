package de.andycandy.protect_me.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtectUtilTest {

	@Test
	public void testProtect() throws Exception {

		List<String> list = Arrays.asList("1", "2", "3");

		@SuppressWarnings("unchecked")
		Iterable<String> it = ProtectUtil.protect(list, Iterable.class);

		List<String> testList = new ArrayList<>();
		for (String s : it) {
			testList.add(s);
		}

		Assertions.assertThat(testList).isEqualTo(list);
	}

	@Test
	public void testProtect_NoCastBack() throws Exception {

		List<String> list = Arrays.asList("1", "2", "3");

		Assertions.assertThatThrownBy(() -> {
			@SuppressWarnings("unchecked")
			List<String> l = (List<String>) ProtectUtil.protect(list, Iterable.class);
			l.hashCode();
		}).isInstanceOf(ClassCastException.class);
	}
	
	@Test
	public void testProtect_NoListMethod() throws Exception {

		List<String> list = Arrays.asList("1", "2", "3");

		@SuppressWarnings("unchecked")
		Iterable<String> it = ProtectUtil.protect(list, Iterable.class);
		
		Method getMethod = List.class.getMethod("get", int.class);
		Assertions.assertThatThrownBy(() -> getMethod.invoke(it, new Object[] { Integer.valueOf(0) })).isInstanceOf(IllegalArgumentException.class);
	}


	@Test
	public void testProtect_WithException() throws Exception {

		List<String> list = Arrays.asList("1", "2", "3");

		@SuppressWarnings("unchecked")
		List<String> protectedList = ProtectUtil.protect(list, List.class);
		
		Assertions.assertThatThrownBy(() -> protectedList.get(3)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
	}
}
