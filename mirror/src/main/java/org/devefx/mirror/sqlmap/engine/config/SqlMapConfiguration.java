package org.devefx.mirror.sqlmap.engine.config;

import java.util.Iterator;

import org.devefx.mirror.common.reflection.ReflectionUtils;
import org.devefx.mirror.sqlmap.annotation.Table;
import org.devefx.mirror.sqlmap.client.SqlMapClient;
import org.devefx.mirror.sqlmap.client.impl.SqlMapClientImpl;
import org.devefx.mirror.sqlmap.client.impl.SqlMapExecutorDelegate;

public class SqlMapConfiguration {
	private String entityPackage;
	
	private SqlMapExecutorDelegate delegate;
	private SqlMapClient client;
	
	public SqlMapConfiguration() {
		delegate = new SqlMapExecutorDelegate();
		client = new SqlMapClientImpl(delegate);
	}
	
	public SqlMapClient getClient() {
		return client;
	}
	
	public SqlMapExecutorDelegate getDelegate() {
		return delegate;
	}
	
	public void finalizeSqlMapConfig() {
		scanModelMapper();
	}
	
	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}
	
	private void scanModelMapper() {
		Iterator<Class<?>> it = ReflectionUtils.getClasses(entityPackage).iterator();
		while (it.hasNext()) {
			Class<?> clazz = it.next();
			if (clazz.isAnnotationPresent(Table.class)) {
				delegate.resolveMapper(clazz);
			}
		}
	}
}
