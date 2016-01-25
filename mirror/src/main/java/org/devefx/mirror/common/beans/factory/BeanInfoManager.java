package org.devefx.mirror.common.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.devefx.mirror.common.beans.factory.BeanInfo.Ref;

public class BeanInfoManager {
	
	private List<BeanInfo> beanInfos;
	private Map<String, Ref> refMap;
	
	public BeanInfoManager() {
		beanInfos = new ArrayList<BeanInfo>();
		refMap = new HashMap<String, Ref>();
	}
	
	public void addBeanInfo(BeanInfo beanInfo) {
		Ref ref = refMap.get(beanInfo.getBeanName());
		if (ref != null) {
			ref.setBeanInfo(beanInfo);
		}
		beanInfos.add(beanInfo);
	}
	
	public BeanInfo getBeanInfo(String name, Class<?> requiredType) {
		Iterator<BeanInfo> iterator = beanInfos.iterator();
		while (iterator.hasNext()) {
			BeanInfo beanInfo = iterator.next();
			if ((requiredType == null || requiredType == beanInfo.getBeanClass()) &&
					((name == beanInfo.getBeanName())) || (name != null && name.equals(beanInfo.getBeanName()))) {
				return beanInfo;
			}
		}
		return null;
	}
	/**
	 * 获取一个Bean引用，不存在则创建一个
	 * @param beanName
	 * @return Ref
	 */
	public Ref takeRef(String beanName) {
		if (beanName != null && !beanName.isEmpty()) {
			Ref ref = refMap.get(beanName);
			if (ref == null) {
				ref = new Ref(beanName);
				ref.setBeanInfo(getBeanInfo(beanName, null));
				refMap.put(beanName, ref);
			}
			return ref;
		}
		return null;
	}
	/**
	 * 构建bean
	 * @throws BeanCreateException void
	 */
	public List<BeanInfo> build() throws BeanCreateException {
		List<BeanInfo> list = new ArrayList<BeanInfo>();
		while (!beanInfos.isEmpty()) {
			boolean created = false;
			BeanCreateException exception = null;
			for (int i = beanInfos.size() - 1; i != -1; i--) {
				BeanInfo beanInfo = beanInfos.get(i);
				try {
					if (beanInfo.build()) {
						list.add(beanInfo);
						beanInfos.remove(i);
						created = true;
					}
				} catch (BeanCreateException e) {
					exception = e;
				}
			}
			if (!created) {
				throw exception;
			}
		}
		for (BeanInfo beanInfo : list) {
			try {
				beanInfo.init();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
