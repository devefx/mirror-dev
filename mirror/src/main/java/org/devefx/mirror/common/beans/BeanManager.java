package org.devefx.mirror.common.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanManager {
	
	private Map<String, Object> beanNameMap;
	private Map<Class<?>, List<Object>> beanTypeMap;
	
	public BeanManager() {
		beanNameMap = new HashMap<String, Object>();
		beanTypeMap = new HashMap<Class<?>, List<Object>>();
	}
	
	public Object getBean(String name) {
		return beanNameMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		Object bean = beanNameMap.get(name);
		if (bean == null) {
			throw new BeansException("No bean named '" + name + "' is defined");
		} else {
			Class<?> type = bean.getClass();
			if (requiredType.isAssignableFrom(type)) {
				return (T) bean;
			}
			throw new BeansException("Bean named '" + name + "' must be of type [" + requiredType.getName() + "]," +
					" but was actually of type [" + type.getName() +"]");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		List<Object> list = beanTypeMap.get(requiredType);
		if (list == null) {
			return null;
		} else if (list.size() == 1) {
			return (T) list.get(0);
		}
		throw new BeansException("No qualifying bean of type [" + requiredType.getName() +"] is defined");
	}
	
	public void registry(String name, Object bean) {
		if (name != null) {
			beanNameMap.put(name, bean);
		}
		if (bean != null) {
			Class<?> type = bean.getClass();
			List<Object> list = beanTypeMap.get(type);
			if (list == null) {
				list = new ArrayList<Object>();
				beanTypeMap.put(type, list);
			}
			list.add(bean);
		}
	}
}
