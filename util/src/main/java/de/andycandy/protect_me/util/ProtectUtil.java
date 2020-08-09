package de.andycandy.protect_me.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ProtectUtil {

	/**
	 * This method creates a proxy which delegates all methods defined in the
	 * provided class to the object t. Furthermore methods defined in the Object
	 * class will be delegated as well. The method return implements the provided class.
	 * 
	 * @param <T> typeparameter
	 * @param t object for delegation
	 * @param clazz provided class
	 * @return a delegating proxy
	 */

	@SuppressWarnings("unchecked")
	public static <T> T protect(T t, Class<T> clazz) {

		Set<Method> methodSet = new HashSet<>(Arrays.asList(clazz.getMethods()));
		methodSet.addAll(Arrays.asList(Object.class.getMethods()));

		return (T) Proxy.newProxyInstance(t.getClass().getClassLoader(), new Class[] { clazz },
				(proxy, method, args) -> {
					if (!methodSet.contains(method)) {
						throw new NoSuchMethodException();
					}

					try {						
						return method.invoke(t, args);
					}
				    catch (InvocationTargetException e) {
				        throw e.getCause();
				    }
				});
	}

}
