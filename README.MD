# ProtectMe

This is a simple library which allows you to protect your java object with a interface. This can be helpful if you don't want to offer the full access to a object from outside. For example if you write a library and you wants to have all modifications under control to ensure that the library works as expected.

The following example shows how easy you can protect a list with a ast annotation.
```
class TestMethodAnnotation {
	
	List<String> list = []
	
	@Protect
	Iterable getProtectedIterable() {
		list
	}
	
	Iterable getIterable() {
		list
	}
	
}
```

```
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
```
It's possible to change modify the list with the not protected iterable. The object is still a List can all methods can be executed. The protected iterabel just offers the methods of a itrable. So it's not possible to modify the list.

The idea is that the object is wrapped by a proxy and we declare the method which will be delegated to the object by a interface.

```
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
```
This is done with the ProtectUtil class, which is available for java and groovy. The generated proxy also delegates the methods of the class Object. So mehtods like equals and toString should still work.
The @Protect ast annotation can be used in groovy and can be defined on types and methods.

## License

MIT License

Copyright (c) 2020 the andycandy-de

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.