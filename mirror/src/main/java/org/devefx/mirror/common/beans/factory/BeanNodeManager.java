package org.devefx.mirror.common.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.devefx.mirror.common.beans.factory.BeanNode.Ref;

public class BeanNodeManager {
	
	private List<BeanNode> beanNodes;
	private Map<String, Ref> refMap;
	
	public BeanNodeManager() {
		beanNodes = new ArrayList<BeanNode>();
		refMap = new HashMap<String, Ref>();
	}
	
	public void addBeanNode(BeanNode beanNode) {
		Ref ref = refMap.get(beanNode.getBeanName());
		if (ref != null) {
			ref.setBeanNode(beanNode);
		}
		beanNodes.add(beanNode);
	}
	
	public BeanNode getBeanNote(String name, Class<?> requiredType) {
		Iterator<BeanNode> iterator = beanNodes.iterator();
		while (iterator.hasNext()) {
			BeanNode beanNode = iterator.next();
			if ((requiredType == null || requiredType == beanNode.getBeanClass()) &&
					((name == beanNode.getBeanName())) || (name != null && name.equals(beanNode.getBeanName()))) {
				return beanNode;
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
				ref.setBeanNode(getBeanNote(beanName, null));
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
	public List<BeanNode> build() throws BeanCreateException {
		List<BeanNode> list = new ArrayList<BeanNode>();
		while (!beanNodes.isEmpty()) {
			boolean created = false;
			BeanCreateException exception = null;
			for (int i = beanNodes.size() - 1; i != -1; i--) {
				BeanNode beanNode = beanNodes.get(i);
				try {
					if (beanNode.build()) {
						list.add(beanNode);
						beanNodes.remove(i);
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
		for (BeanNode beanNode : list) {
			try {
				beanNode.init();
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
