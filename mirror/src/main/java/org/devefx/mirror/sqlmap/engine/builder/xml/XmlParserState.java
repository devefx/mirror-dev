package org.devefx.mirror.sqlmap.engine.builder.xml;

import java.util.Properties;

import javax.sql.DataSource;

import org.devefx.mirror.common.beans.BeanManager;
import org.devefx.mirror.common.resources.Resources;

public class XmlParserState {
	
	private Properties globalProps = new Properties();
	private DataSource dataSource;
	private BeanManager beanManager = new BeanManager();

	public Properties getGlobalProps() {
		return globalProps;
	}
	
	public void setGlobalProps(Properties globalProps) {
		this.globalProps = globalProps;
	}
	
	public void addGlobalProperties(String resource, String url) {
		try {
			Properties props = null;
			if (resource != null) {
				props = Resources.getResourceAsProperties(resource);
			} else if (url != null) {
				props = Resources.getUrlAsProperties(url);
			} else {
				throw new RuntimeException("The " + "properties" + " element requires either a resource or a url attribute.");
			}
			if (props != null) {
				globalProps.putAll(props);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error loading properties.  Cause: " + e, e);
		}
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public BeanManager getBeanManager() {
		return beanManager;
	}
}
