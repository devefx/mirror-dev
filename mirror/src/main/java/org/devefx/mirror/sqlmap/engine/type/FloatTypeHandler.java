package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FloatTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object f = rs.getFloat(columnName);
		if (rs.wasNull())
			return null;
		return f;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object f = rs.getFloat(columnIndex);
		if (rs.wasNull())
			return null;
		return f;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object f = cs.getFloat(columnIndex);
		if (cs.wasNull())
			return null;
		return f;
	}

	@Override
	public Object valueOf(String s) {
		return Float.valueOf(s);
	}
}
