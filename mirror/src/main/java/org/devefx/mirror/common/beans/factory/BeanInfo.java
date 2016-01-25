package org.devefx.mirror.common.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.devefx.mirror.common.reflection.factory.ObjectFactory;
import org.devefx.mirror.common.reflection.invoker.MethodInvoker;
import org.devefx.mirror.common.reflection.invoker.SetMethodInvoker;

/**
 * BeanInfo
 * @author： youqian.yue
 * @date： 2016-1-19 下午2:22:25
 */
public class BeanInfo {
	
	private Object bean;
	private boolean created;
	/**
	 * bean信息
	 */
	private String beanName;
	private Class<?> beanClass;
	private String initMethod;
	/**
	 * bean参数
	 */
	private Map<Integer, Object> constructorArgMap;
	private Map<String, Object> propertyMap;
	/**
	 * 记录变量
	 */
	private int constructorArgIndex;
	private String propertyName;
	
	public BeanInfo(String beanName, Class<?> beanClass, String initMethod) {
		this.beanName = beanName;
		this.beanClass = beanClass;
		this.initMethod = initMethod;
		constructorArgMap = new HashMap<Integer, Object>();
		propertyMap = new HashMap<String, Object>();
	}
	public Object getBean() {
		return bean;
	}
	public boolean isCreated() {
		return created;
	}
	public String getBeanName() {
		return beanName;
	}
	public Class<?> getBeanClass() {
		return beanClass;
	}
	/**
	 * 设置构造参数值
	 * @param index
	 * @param value
	 */
	public void setConstructorArg(String index, Object value) {
		try {
			constructorArgIndex = Integer.parseInt(index);
			constructorArgMap.put(constructorArgIndex, value);
		} catch (NumberFormatException e) {
			constructorArgMap.put(constructorArgIndex++, value);
		}
	}
	public void setConstructorArg(Object value) {
		constructorArgMap.put(constructorArgIndex++, value);
	}
	@SuppressWarnings("unchecked")
	public void addConstructorArgToList(Object value) {
		((List<Object>) constructorArgMap.get(constructorArgIndex-1)).add(value);
	}
	@SuppressWarnings("unchecked")
	public void addConstructorArgToMap(Object key, Object value) {
		((Map<Object, Object>) constructorArgMap.get(constructorArgIndex-1)).put(key, value);
	}
	/**
	 * 设置成员属性值
	 * @param name
	 * @param value
	 */
	public void setProperty(String name, Object value) {
		propertyName = name;
		propertyMap.put(propertyName, value);
	}
	public void setProperty(Object value) {
		propertyMap.put(propertyName, value);
	}
	@SuppressWarnings("unchecked")
	public void addPropertyToList(Object value) {
		((List<Object>) propertyMap.get(propertyName)).add(value);
	}
	@SuppressWarnings("unchecked")
	public void addPropertyToMap(Object key, Object value) {
		((Map<Object, Object>) propertyMap.get(propertyName)).put(key, value);
	}
	/**
	 * 构建Bean
	 * @return boolean
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean build() throws BeanCreateException {
		//　create object
		if (constructorArgMap.size() > 0) {
			Object[] constructorArgs = new Object[constructorArgMap.size()];
			for (Map.Entry<Integer, Object> entry : constructorArgMap.entrySet()) {
				Object val = entry.getValue();
				if (val instanceof Ref) {
					BeanInfo beanInfo = ((Ref) val).getBeanInfo();
					if (beanInfo != null && beanInfo.isCreated()) {
						val = beanInfo.getBean();
						constructorArgMap.put(entry.getKey(), val);
					} else {
						throw new BeanCreateException("No bean named '" + ((Ref) val).getBeanName() + "' is defined");
					}
				} else {
					ArgHandler handler = handlerMap.get(val.getClass());
					if (handler != null) {
						handler.handler(val);
					}
				}
				constructorArgs[entry.getKey()] = val;
			}
			bean = new ObjectFactory().create(beanClass, null, Arrays.asList(constructorArgs));
		} else {
			bean = new ObjectFactory().create(beanClass);
		}
		created = true;
		return true;
	}
	/**
	 * 初始化bean
	 * @return boolean
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws BeanCreateException 
	 * @throws NoSuchMethodException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean init() throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, BeanCreateException {
		// set property
		for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof Ref) {
				BeanInfo beanInfo = ((Ref) val).getBeanInfo();
				if (beanInfo != null && beanInfo.isCreated()) {
					new SetMethodInvoker(key).invoke(bean, beanInfo.getBean());
				} else {
					throw new BeanCreateException("Error creating bean with name '" + beanName + "' instantiation of bean failed." +
							"  Cause: No bean named '" + ((Ref) val).getBeanName() + "' is defined");
				}
			} else {
				ArgHandler handler = handlerMap.get(val.getClass());
				if (handler != null) {
					handler.handler(val);
				}
				new SetMethodInvoker(key).invoke(bean, val);
			}
		}
		if (initMethod != null && !initMethod.isEmpty()) {
			new MethodInvoker(initMethod).invoke(bean);
		}
		return true;
	}
	
	// ArgHandler
	private interface ArgHandler<T> {
		void handler(T value) throws BeanCreateException;
	}
	private static Map<Class<?>, ArgHandler<?>> handlerMap = new HashMap<Class<?>, ArgHandler<?>>();
	static {
		handlerMap.put(ArrayList.class, new ArgHandler<List<Object>>() {
			@Override
			public void handler(List<Object> list) throws BeanCreateException {
				for (int i = 0, n = list.size(); i < n; i++) {
					Object val = list.get(0);
					if (val instanceof Ref) {
						BeanInfo beanInfo = ((Ref) val).getBeanInfo();
						if (beanInfo != null && beanInfo.isCreated()) {
							list.set(i, beanInfo.getBean());
						} else {
							throw new BeanCreateException("No bean named '" + ((Ref) val).getBeanName() + "' is defined");
						}
					}
				}
			}
		});
		handlerMap.put(HashMap.class, new ArgHandler<Map<Object, Object>>() {
			@Override
			public void handler(Map<Object, Object> map) throws BeanCreateException {
				for (Map.Entry<Object, Object> entry : map.entrySet()) {
					Object key = entry.getKey();
					Object val = entry.getValue();
					if (key instanceof Ref) {
						BeanInfo beanInfo = ((Ref) key).getBeanInfo();
						if (beanInfo != null && beanInfo.isCreated()) {
							map.remove(key);
							key = beanInfo.getBean();
							map.put(key, val);
						} else {
							throw new BeanCreateException("No bean named '" + ((Ref) key).getBeanName() + "' is defined");
						}
					}
					if (val instanceof Ref) {
						BeanInfo beanInfo = ((Ref) val).getBeanInfo();
						if (beanInfo != null && beanInfo.isCreated()) {
							val = beanInfo.getBean();
							map.put(key, val);
						} else {
							throw new BeanCreateException("No bean named '" + ((Ref) val).getBeanName() + "' is defined");
						}
					}
				}
			}
		});
	}
	// Ref
	public static class Ref {
		private String beanName;
		private BeanInfo beanInfo;
		public Ref(String beanName) {
			this.beanName = beanName;
		}
		public String getBeanName() {
			return beanName;
		}
		public BeanInfo getBeanInfo() {
			return beanInfo;
		}
		public void setBeanInfo(BeanInfo beanInfo) {
			this.beanInfo = beanInfo;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((beanName == null) ? 0 : beanName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Ref other = (Ref) obj;
			if (beanName == null) {
				if (other.beanName != null)
					return false;
			} else if (!beanName.equals(other.beanName))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "Ref [beanName=" + beanName + "]";
		}
	}
}
