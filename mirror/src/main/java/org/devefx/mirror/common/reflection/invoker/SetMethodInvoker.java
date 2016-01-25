package org.devefx.mirror.common.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.devefx.mirror.common.reflection.ReflectionUtils;

public class SetMethodInvoker extends MethodInvoker {
	
	public SetMethodInvoker(Method method) {
		super(method);
	}
	public SetMethodInvoker(String name) {
		super(name);
	}
	@Override
	public Object invoke(Object target, Object ...args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (method == null) {
			name = ReflectionUtils.toSetter(name);
		}
		return super.invoke(target, args);
	}
}
