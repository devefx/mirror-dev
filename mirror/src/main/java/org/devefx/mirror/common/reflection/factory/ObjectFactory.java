package org.devefx.mirror.common.reflection.factory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.devefx.mirror.common.reflection.ReflectionException;
import org.devefx.mirror.common.reflection.ReflectionUtils;

public class ObjectFactory {
	
	public <T> T create(Class<T> type) {
		return create(type, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
		return (T) instantiateClass(resolveInterface(type), constructorArgTypes, constructorArgs);
	}
	
	private Object instantiateClass(Class<?> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
		try {
			Constructor<?> constructor = null;
			if (constructorArgTypes == null && constructorArgs == null) {
				constructor = type.getDeclaredConstructor();
				if (!constructor.isAccessible()) {
					constructor.setAccessible(true);
				}
				return constructor.newInstance();
			}
			try {
				constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
			} catch (Exception e) {
				for (Constructor<?> c : type.getDeclaredConstructors()) {
					Class<?>[] parameterTypes = c.getParameterTypes();
					if (parameterTypes.length == constructorArgs.size()) {
						if (ReflectionUtils.convertParameter(parameterTypes, constructorArgs))
							constructor = c;
					}
				}
			}
			if (!constructor.isAccessible()) {
				constructor.setAccessible(true);
			}
			return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
		} catch (Exception e) {
			StringBuilder argTypes = new StringBuilder();
			if (constructorArgTypes != null && !constructorArgTypes.isEmpty()) {
				for (Class<?> argType : constructorArgTypes) {
					argTypes.append(argType.getSimpleName());
					argTypes.append(",");
				}
				argTypes.deleteCharAt(argTypes.length() - 1);
			}
			StringBuilder argValues = new StringBuilder();
			if (constructorArgs != null && !constructorArgs.isEmpty()) {
				for (Object argValue : constructorArgs) {
					argValues.append(String.valueOf(argValue));
					argValues.append(",");
				}
				argValues.deleteCharAt(argValues.length() - 1);
			}
			throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + e, e);
		}
	}
	
	protected Class<?> resolveInterface(Class<?> type) {
		if (type == List.class || type == Collection.class || type == Iterable.class) {
			return ArrayList.class;
		} else if (type == Map.class) {
			return HashMap.class;
		} else if (type == SortedSet.class) {
			return TreeSet.class;
		} else if (type == Set.class) {
			return HashSet.class;
		}
		return type;
	}
	
}
