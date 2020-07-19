package de.andycandy.protect_me.util;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ProtectUtil {

	@SuppressWarnings("unchecked")
	public static <T> T protect(T t, Class<T> clazz) {

		Set<Method> methodSet = new HashSet<>(Arrays.asList(clazz.getMethods()));
		methodSet.addAll(Arrays.asList(Object.class.getMethods()));

		return (T) Proxy.newProxyInstance(ProtectUtil.class.getClassLoader(), new Class[] { clazz },
				(proxy, method, args) -> {
					if (!methodSet.contains(method)) {
						throw new NoSuchMethodException();
					}

					return method.invoke(t, args);
				});
	}

}
