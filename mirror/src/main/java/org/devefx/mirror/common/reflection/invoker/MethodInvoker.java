package org.devefx.mirror.common.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.devefx.mirror.common.reflection.ReflectionUtils;

public class MethodInvoker {
	
	protected Method method;
	protected String name;
	
	public MethodInvoker(Method method) {
		this.method = method;
	}
	public MethodInvoker(String name) {
		this.name = name;
	}
	
	public Object invoke(Object target, Object ...args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (method == null) {
			method = ReflectionUtils.findMethod(target.getClass(),
					name, null, Arrays.asList(args));
		}
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
		return method.invoke(target, args);
	}
}
