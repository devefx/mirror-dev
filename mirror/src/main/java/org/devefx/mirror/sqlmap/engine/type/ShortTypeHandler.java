package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ShortTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object s = rs.getShort(columnName);
		if (rs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object s = rs.getShort(columnIndex);
		if (rs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object s = cs.getShort(columnIndex);
		if (cs.wasNull())
			return null;
		return s;
	}

	@Override
	public Object valueOf(String s) {
		return Short.valueOf(s);
	}
}
