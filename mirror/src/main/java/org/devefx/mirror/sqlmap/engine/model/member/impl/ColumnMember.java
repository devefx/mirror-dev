package org.devefx.mirror.sqlmap.engine.model.member.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.devefx.mirror.sqlmap.engine.model.member.Member;

public class ColumnMember implements Member {

	private String name;
	private Class<?> type;
	private boolean isCollection;
	
	public ColumnMember(Field field) {
		name = field.getName();
		type = field.getType();
		if (Collection.class.isAssignableFrom(type)) {
			Type type = field.getGenericType();
			if (type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) type;
				this.type = (Class<?>) pt.getActualTypeArguments()[0];
			}
			isCollection = true;
		}
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean isCollection() {
		return isCollection;
	}
}
