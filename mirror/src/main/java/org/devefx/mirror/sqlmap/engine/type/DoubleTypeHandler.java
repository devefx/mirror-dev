package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DoubleTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object d = rs.getDouble(columnName);
		if (rs.wasNull())
			return null;
		return d;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object d = rs.getDouble(columnIndex);
		if (rs.wasNull())
			return null;
		return d;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object d = cs.getDouble(columnIndex);
		if (cs.wasNull())
			return null;
		return d;
	}

	@Override
	public Object valueOf(String s) {
		return Double.valueOf(s);
	}
}
