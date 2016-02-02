package org.devefx.mirror.sqlmap.client.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.devefx.mirror.sqlmap.annotation.Column;
import org.devefx.mirror.sqlmap.annotation.Table;
import org.devefx.mirror.sqlmap.client.SqlMapException;
import org.devefx.mirror.sqlmap.engine.mapping.ModelMapper;
import org.devefx.mirror.sqlmap.engine.mapping.state.ColumnStatement;
import org.devefx.mirror.sqlmap.engine.mapping.state.TableStatement;

public class SqlMapExecutorDelegate {
	
	private Map<Class<?>, ModelMapper> mapperClass;
	private Map<String, ModelMapper> mapperTable;
	
	public SqlMapExecutorDelegate() {
		mapperClass = new HashMap<Class<?>, ModelMapper>();
		mapperTable = new HashMap<String, ModelMapper>();
	}
	
	public void addModelMapper(ModelMapper mapper) {
		if (mapperTable.containsKey(mapper.getTableName())) {
			throw new SqlMapException("There is already a mapper named " + mapper.getTableName() + " in this SqlMap");
		}
		mapperClass.put(mapper.getModelClass(), mapper);
		mapperTable.put(mapper.getTableName(), mapper);
	}
	
	public ModelMapper getModelMapper(String tableName) {
		ModelMapper mapper = mapperTable.get(tableName);
		if (mapper == null) {
			throw new SqlMapException("There is no mapper named " + tableName + " in this SqlMap.");
		}
		return mapper;
	}
	
	public ModelMapper getModelMapper(Class<?> modelClass) {
		ModelMapper mapper = mapperClass.get(modelClass);
		if (mapper == null) {
			throw new SqlMapException("There is no mapper class [" + modelClass + "] in this SqlMap.");
		}
		return mapper;
	}
	
	public void resolveMapper(Class<?> modelClass) {
		ModelMapper mapper = mapperClass.get(modelClass);
		if (mapper == null) {
			mapper = new ModelMapper();
			mapper.setModelClass(modelClass);
			addModelMapper(mapper);
		}
		// find Column & Table
		for (Field field : modelClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class)) {
				String column = field.getAnnotation(Column.class).value();
				if (column == null || column.isEmpty()) {
					column = field.getName();
				}
				mapper.addMapping(column, new ColumnStatement(modelClass, field));
			} else if (field.isAnnotationPresent(Table.class)) {
				Table table = field.getAnnotation(Table.class);
				ModelMapper _mapper = mapperClass.get(field.getType());
				if (_mapper == null) {
					_mapper = new ModelMapper();
					_mapper.setModelClass(field.getType());
					addModelMapper(_mapper);
				}
				mapper.addMapping(field.getName(), new TableStatement(table.value(), _mapper));
			}
		}
	}
}
