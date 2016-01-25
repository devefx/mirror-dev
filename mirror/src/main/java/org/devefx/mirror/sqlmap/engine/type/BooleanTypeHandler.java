package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BooleanTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object b = rs.getBoolean(columnName);
		if (rs.wasNull())
			return null;
		return b;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object b = rs.getBoolean(columnIndex);
		if (rs.wasNull())
			return null;
		return b;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object b = cs.getBoolean(columnIndex);
		if (cs.wasNull())
			return null;
		return b;
	}

	@Override
	public Object valueOf(String s) {
		return Boolean.valueOf(s);
	}
}
