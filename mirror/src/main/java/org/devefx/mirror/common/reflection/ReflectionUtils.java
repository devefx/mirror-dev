package org.devefx.mirror.common.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.devefx.mirror.sqlmap.engine.type.TypeHandler;
import org.devefx.mirror.sqlmap.engine.type.TypeHandlerFactory;

public class ReflectionUtils {
	
	private static final TypeHandlerFactory FACTORY = new TypeHandlerFactory();
	
	public static boolean isGetter(String name) {
		return name.startsWith("get") || name.startsWith("is");
	}
	
	public static boolean isSetter(String name) {
		return name.startsWith("set");
	}
	
	public static String toSetter(String name) {
		if (!isGetter(name)) {
			return "set" + name.substring(0, 1).toUpperCase()
					+ (name.length() != 1 ? name.substring(1) : "");
		}
		return name;
	}
	
	public static String toGetter(Class<?> clazz, String name) {
		if (!isGetter(name)) {
			String newName = name.substring(0, 1).toUpperCase();
			if (name.length() != 1)
				newName += name.substring(1);
			try {
				Class<?> type = clazz.getDeclaredField(name).getType();
				return (type == boolean.class || type == Boolean.class ? "is" : "get")
						+ newName;
			} catch (NoSuchFieldException e) {
				return newName;
			}
		}
		return name;
	}
	
	public static Method findMethod(Class<?> clazz, String name, List<Class<?>> argTypes, List<Object> args) throws ReflectionException {
		try {
			if (argTypes == null || argTypes.size() != args.size()) {
				if (argTypes == null) {
					argTypes = new ArrayList<Class<?>>();
				} else {
					argTypes.clear();
				}
				for (Object arg : args)
					argTypes.add(arg.getClass());
			}
			return clazz.getDeclaredMethod(name, argTypes.toArray(new Class<?>[argTypes.size()]));
		} catch (NoSuchMethodException e) {
			Class<?> _clazz = clazz;
			while (_clazz != null && _clazz != Object.class) {
				for (Method method : _clazz.getDeclaredMethods()) {
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length == args.size() && method.getName().endsWith(name)) {
						if (convertParameter(parameterTypes, args)) {
							if (argTypes != null) {
								argTypes.clear();
								for (Class<?> type : parameterTypes)
									argTypes.add(type);
							}
							return method;
						}
					}
				}
				_clazz = _clazz.getSuperclass();
			}
		}
		// not found method
		StringBuffer _argTypes = new StringBuffer();
		if (args != null && args.size() != 0) {
			for (Object arg : args) {
				_argTypes.append(arg != null ? arg.getClass().getSimpleName() : "*");
				_argTypes.append(",");
			}
			_argTypes.deleteCharAt(_argTypes.length() - 1);
		}
		throw new ReflectionException("not found " + clazz.getName() + ".*" + name + "(" + _argTypes + ")");
	}
	
	public static Object convertType(Class<?> type, Object value) {
		if (value != null && !type.isAssignableFrom(value.getClass())) {
			TypeHandler handler = FACTORY.getTypeHandler(type);
			if (handler != null) {
				try {
					return handler.valueOf(value.toString());
				} catch (Exception e) {
				}
			}
			return null;
		}
		return value;
	}
	
	public static boolean convertParameter(Class<?>[] parameterTypes, List<Object> parameters) {
		if (parameterTypes != null && parameters != null &&
				parameterTypes.length == parameters.size()) {
			for (int i = 0, n = parameterTypes.length; i < n; i++) {
				Object value = parameters.get(i);
				if (value != null) {
					Object convertValue = convertType(parameterTypes[i], value);
					if (convertValue == null) {
						return false;
					}
					parameters.set(i, convertValue);
				}
			}
			return true;
		}
		return false;
	}
}
