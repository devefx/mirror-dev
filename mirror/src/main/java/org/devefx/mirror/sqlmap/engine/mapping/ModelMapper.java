package org.devefx.mirror.sqlmap.engine.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.devefx.mirror.sqlmap.annotation.Table;
import org.devefx.mirror.sqlmap.engine.mapping.sql.Sql;
import org.devefx.mirror.sqlmap.engine.mapping.sql.SqlBuilder;
import org.devefx.mirror.sqlmap.engine.mapping.sql.SqlBuilderFactory;
import org.devefx.mirror.sqlmap.engine.mapping.sql.SqlBuilderType;
import org.devefx.mirror.sqlmap.engine.mapping.state.ColumnStatement;
import org.devefx.mirror.sqlmap.engine.mapping.state.TableStatement;

public class ModelMapper {
	
	private static final SqlBuilderFactory sqlBuilderFactory = new SqlBuilderFactory();
	
	private Class<?> modelClass;
	private String tableName;
	private String primaryKey;
	
	private Map<String, List<String>> mappedProperty = new HashMap<String, List<String>>(); // column -> property
	private Map<String, ColumnStatement> mappedColumn = new HashMap<String, ColumnStatement>(); // property -> column
	private Map<String, TableStatement> mappedTable = new HashMap<String, TableStatement>(); // property -> table
	private Map<SqlBuilderType, Sql> sqlCache = new HashMap<SqlBuilderType, Sql>();
	
	public Class<?> getModelClass() {
		return modelClass;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}
	
	public void setModelClass(Class<?> modelClass) {
		try {
			Table table = modelClass.getAnnotation(Table.class);
			this.modelClass = modelClass;
			tableName = table.value();
			primaryKey = table.key();
		} catch (Exception e) {
			throw new ModelMapperException("class [" + modelClass.getName() + "] not used @Table");
		}
	}
	
	public void addMapping(String column, ColumnStatement statement) {
		String property = statement.getName();
		mappedColumn.put(property, statement);
		List<String> list = mappedProperty.get(column);
		if (list == null) {
			list = new ArrayList<String>();
			mappedProperty.put(column, list);
		}
		list.add(property);
	}
	
	public void addMapping(String property, TableStatement statement) {
		mappedTable.put(property, statement);
	}
	
	public Map<String, List<String>> getMappedProperty() {
		return mappedProperty;
	}
	
	public Map<String, ColumnStatement> getMappedColumn() {
		return mappedColumn;
	}
	
	public Map<String, TableStatement> getMappedTable() {
		return mappedTable;
	}
	
	public Iterator<String> getMappedColumnNames() {
		return mappedProperty.keySet().iterator();
	}
	
	public Sql getSql(SqlBuilderType builderType) {
		Sql sql = sqlCache.get(builderType);
		if (sql == null) {
			SqlBuilder builder = sqlBuilderFactory.getSqlBuilder(builderType);
			sql = builder.builder(this);
			sqlCache.put(builderType, sql);
		}
		return sql;
	}
}
