package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StringTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object s = rs.getString(columnName);
		if (rs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object s = rs.getString(columnIndex);
		if (rs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object s = cs.getString(columnIndex);
		if (cs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
