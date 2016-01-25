package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LongTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object l = rs.getLong(columnName);
		if (rs.wasNull())
			return null;
		return l;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object l = rs.getLong(columnIndex);
		if (rs.wasNull())
			return null;
		return l;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object l = cs.getLong(columnIndex);
		if (cs.wasNull())
			return null;
		return l;
	}

	@Override
	public Object valueOf(String s) {
		return Long.valueOf(s);
	}
}
