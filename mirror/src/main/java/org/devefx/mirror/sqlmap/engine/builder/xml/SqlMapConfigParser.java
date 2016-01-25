package org.devefx.mirror.sqlmap.engine.builder.xml;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.devefx.mirror.common.beans.factory.BeanCreateException;
import org.devefx.mirror.common.beans.factory.BeanInfo;
import org.devefx.mirror.common.beans.factory.BeanInfoManager;
import org.devefx.mirror.common.beans.factory.BeanInfo.Ref;
import org.devefx.mirror.common.resources.Resources;
import org.devefx.mirror.common.xml.Nodelet;
import org.devefx.mirror.common.xml.NodeletParser;
import org.devefx.mirror.common.xml.NodeletUtils;
import org.w3c.dom.Node;

public class SqlMapConfigParser {
	
	protected final NodeletParser parser = new NodeletParser();
	private XmlParserState state = new XmlParserState();
	
	public SqlMapConfigParser() {
		addGlobalPropNodelets();
		addBeanCreateNodelets();
		addSettingNodelets();
	}
	
	public void parse(Reader reader) {
		try {
			parser.parse(reader);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred.  Cause: " + e, e);
		}
	}
	
	public void parse(InputStream inputStream) {
		try {
			parser.parse(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred.  Cause: " + e, e);
		}
	}
	
	private void addGlobalPropNodelets() {
		parser.addNodelet("/mirror/property-file", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attributes = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				String resource = attributes.getProperty("resource");
				String url = attributes.getProperty("url");
				state.addGlobalProperties(resource, url);
			}
		});
	}
	
	private void addSettingNodelets() {
		parser.addNodelet("/mirror/setting/@dataSource", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				DataSource dataSource = state.getBeanManager().getBean(node.getNodeValue(),
						DataSource.class);
				state.setDataSource(dataSource);
			}
		});
	}
	
	private void addBeanCreateNodelets() {
		 // temp variable
		final BeanInfoManager beanManager = new BeanInfoManager();
		final BeanInfo temp[] = new BeanInfo[1];
		
		parser.addNodelet("/mirror/bean", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				String beanName = attr.getProperty("id");
				String className = attr.getProperty("class");
				String initMethod = attr.getProperty("init-method");
				try {
					Class<?> beanClass = Resources.classForName(className);
					temp[0] = new BeanInfo(beanName, beanClass, initMethod);
				} catch (Exception e) {
					throw new BeanCreateException("Error creating bean with name '" + beanName + "' instantiation of bean failed." +
							"  Cause: No bean class specified on bean definition");
				}
			}
		});
		
		parser.addNodelet("/mirror/bean/constructor-arg", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				if (!node.hasChildNodes()) {
					Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
					String index = attr.getProperty("index");
					Object value = attr.getProperty("value");
					if (value == null) {
						value = beanManager.takeRef(attr.getProperty("ref"));
					}
					temp[0].setConstructorArg(index, value);
				}
			}
		});
		parser.addNodelet("/mirror/bean/constructor-arg/list", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].setConstructorArg(new ArrayList<>());
			}
		});
		parser.addNodelet("/mirror/bean/constructor-arg/list/value", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].addConstructorArgToList(node.getTextContent());
			}
		});
		parser.addNodelet("/mirror/bean/constructor-arg/list/ref", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				Ref ref = beanManager.takeRef(attr.getProperty("bean"));
				if (ref != null) {
					temp[0].addConstructorArgToList(ref);
				}
			}
		});
		parser.addNodelet("/mirror/bean/constructor-arg/map", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].setConstructorArg(new HashMap<>());
			}
		});
		parser.addNodelet("/mirror/bean/constructor-arg/map/entry", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				Object key = attr.getProperty("key");
				Object value = attr.getProperty("value");
				if (key == null) key = beanManager.takeRef(attr.getProperty("key-ref"));
				if (value == null) value = beanManager.takeRef(attr.getProperty("value-ref"));
				temp[0].addConstructorArgToMap(key, value);
			}
		});
		
		parser.addNodelet("/mirror/bean/property", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				if (!node.hasChildNodes()) {
					Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
					String name = attr.getProperty("name");
					Object value = attr.getProperty("value");
					if (value == null) {
						value = beanManager.takeRef(attr.getProperty("ref"));
					}
					temp[0].setProperty(name, value);
				}
			}
		});
		parser.addNodelet("/mirror/bean/property/list", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].setProperty(new ArrayList<>());
			}
		});
		parser.addNodelet("/mirror/bean/property/list/value", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].setProperty(node.getTextContent());
			}
		});
		parser.addNodelet("/mirror/bean/property/list/ref", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				Ref ref = beanManager.takeRef(attr.getProperty("bean"));
				if (ref != null) {
					temp[0].addPropertyToList(ref);
				}
			}
		});
		parser.addNodelet("/mirror/bean/property/map", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				temp[0].setProperty(new HashMap<>());
			}
		});
		parser.addNodelet("/mirror/bean/property/map/entry", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				Properties attr = NodeletUtils.parseAttributes(node, state.getGlobalProps());
				Object key = attr.getProperty("key");
				Object value = attr.getProperty("value");
				if (key == null) key = beanManager.takeRef(attr.getProperty("key-ref"));
				if (value == null) value = beanManager.takeRef(attr.getProperty("value-ref"));
				temp[0].addPropertyToMap(key, value);
			}
		});
		parser.addNodelet("/mirror/bean/end()", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				beanManager.addBeanInfo(temp[0]);
			}
		});
		
		parser.addNodelet("/mirror/setting", new Nodelet() {
			@Override
			public void process(Node node) throws Exception {
				for (BeanInfo beanInfo : beanManager.build()) {
					state.getBeanManager().registry(beanInfo.getBeanName(), beanInfo.getBean());
				}
			}
		});
	}
}
