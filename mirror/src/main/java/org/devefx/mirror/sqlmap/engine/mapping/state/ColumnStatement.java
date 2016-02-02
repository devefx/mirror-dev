package org.devefx.mirror.sqlmap.engine.mapping.state;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.devefx.mirror.common.reflection.ReflectionUtils;

public class ColumnStatement {
	private Field field;
	private Method setter;
	private Method getter;
	
	public ColumnStatement(Class<?> clazz, Field field) {
		this.field = field;
		try {
			String methodName = ReflectionUtils.toSetter(field.getName());
			setter = clazz.getDeclaredMethod(methodName, field.getType());
		} catch (NoSuchMethodException e) {
		}
		try {
			String methodName = ReflectionUtils.toGetter(field, field.getName());
			getter = clazz.getDeclaredMethod(methodName);
		} catch (Exception e) {
		}
	}
	
	public String getName() {
		return field.getName();
	}
	
	public void setValue(Object obj, Object value) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (setter != null) {
			if (!setter.isAccessible()) {
				setter.setAccessible(true);
			}
			setter.invoke(obj, value);
		} else {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			field.set(obj, value);
		}
	}
	
	public Object getValue(Object obj) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (getter != null) {
			if (!getter.isAccessible()) {
				getter.setAccessible(true);
			}
			return getter.invoke(obj);
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		return field.get(obj);
	}
}
