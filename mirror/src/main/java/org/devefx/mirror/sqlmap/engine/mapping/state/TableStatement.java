package org.devefx.mirror.sqlmap.engine.mapping.state;

import org.devefx.mirror.sqlmap.engine.mapping.ModelMapper;

public class TableStatement {
	private String left;
	private String reight;
	private ModelMapper mapper;
	
	public TableStatement(String joinStr, ModelMapper mapper) {
		this.mapper = mapper;
		String[] join = joinStr.split(":");
		if (!join[0].isEmpty()) {
			left = join[0];
		}
		if (!join[1].isEmpty()) {
			reight = join[1];
		}
	}
	
	public String getLeft() {
		if (left == null) {
			left = mapper.getPrimaryKey();
		}
		return left;
	}
	
	public String getReight() {
		return reight;
	}
	
	public ModelMapper getMapper() {
		return mapper;
	}
}
