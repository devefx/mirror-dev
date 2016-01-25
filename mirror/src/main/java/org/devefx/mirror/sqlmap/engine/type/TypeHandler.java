package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler {
	
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException;
	
	public Object getResult(ResultSet rs, int columnIndex)
			throws SQLException;
	
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException;
	
	public Object valueOf(String s);
	
	public boolean equals(Object object, String string);
}
