package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.devefx.mirror.common.reflection.factory.ObjectFactory;

public class TestBean {
	
	private DataSource dataSource;
	private List<String> list;
	private Map<String, Object> map;
	private String name;
	
	public TestBean() {
	}
	public TestBean(DataSource dataSource, List<String> list, Map<String, Object> map, String name) {
		this.dataSource = dataSource;
		this.list = list;
		this.map = map;
		this.name = name;
	}
	
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) {
		List<Class<?>> constructorArgTypes = new ArrayList<Class<?>>();
		constructorArgTypes.add(DataSource.class);
		constructorArgTypes.add(List.class);
		constructorArgTypes.add(Map.class);
		constructorArgTypes.add(String.class);
		
		List<Object> constructorArgs = new ArrayList<Object>();
		constructorArgs.add(null);
		constructorArgs.add(null);
		constructorArgs.add(null);
		constructorArgs.add(1);
		
		TestBean bean = new ObjectFactory().create(TestBean.class, null, constructorArgs);
		
		
		System.out.println(bean);
		
	}
}
