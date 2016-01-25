package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class IntegerTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object i = rs.getInt(columnName);
		if (rs.wasNull())
			return null;
		return i;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object i = rs.getInt(columnIndex);
		if (rs.wasNull())
			return null;
		return i;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object i = cs.getInt(columnIndex);
		if (cs.wasNull())
			return null;
		return i;
	}

	@Override
	public Object valueOf(String s) {
		return Integer.valueOf(s);
	}
}
