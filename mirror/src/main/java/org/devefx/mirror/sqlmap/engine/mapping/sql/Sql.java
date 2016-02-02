package org.devefx.mirror.sqlmap.engine.mapping.sql;

import java.util.ArrayList;
import java.util.List;

import org.devefx.mirror.sqlmap.engine.mapping.state.ColumnStatement;

public class Sql {

	private String staticSql;
	private List<ColumnStatement> parameters;
	
	public Sql() {
		parameters = new ArrayList<ColumnStatement>();
	}
	
	public List<ColumnStatement> getParameters() {
		return parameters;
	}
	
	
	public void setStaticSql(String staticSql) {
		this.staticSql = staticSql;
	}

	@Override
	public String toString() {
		return "Sql [staticSql=" + staticSql + "]";
	}
}
