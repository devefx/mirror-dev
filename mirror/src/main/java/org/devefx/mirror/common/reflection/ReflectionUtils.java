package org.devefx.mirror.common.reflection;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
			return "set" + firstUpper(name);
		}
		return name;
	}
	
	public static String toGetter(Class<?> clazz, String name) {
		if (!isGetter(name)) {
			try {
				return toGetter(clazz.getDeclaredField(name), name);
			} catch (NoSuchFieldException e) {
			}
		}
		return firstUpper(name);
	}
	
	public static String toGetter(Field field, String name) {
		if (!isGetter(name)) {
			Class<?> type = field.getType();
			return (type == boolean.class || type == Boolean.class ? "is" : "get")
					+ firstUpper(name);
		}
		return name;
	}
	
	public static String firstUpper(String str) {
		if (str != null && !str.isEmpty()) {
			String newStr = str.substring(0, 1).toUpperCase();
			if (str.length() != 1)
				newStr += str.substring(1);
			return newStr;
		}
		return str;
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
	
	/**
	 * 扫描包里面的全部Class
	 * @param basePackage
	 * @return Set<Class<?>>
	 */
	public static Set<Class<?>> getClasses(String basePackage) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		
		String packageName = basePackage;
		String packageDirName = packageName.replace('.', '/');
		
		Enumeration<URL> dirs = null;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findClasses(classes, packageName, filePath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	private static void findClasses(Set<Class<?>> classes, String packageName, String packagePath) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || (file.isFile() && file.getName().endsWith(".class"));
			}
		});
		for (File file : files) {
			if (file.isDirectory()) {
				findClasses(classes, packageName + "." + file.getName(), file.getPath());
			} else {
				String className = file.getName();
				className = className.substring(0, className.length() - 6);
				try {
					ClassLoader loader = Thread.currentThread().getContextClassLoader();
					classes.add(loader.loadClass(packageName + "." + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
