package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClobTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object clob = rs.getClob(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return clob;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object clob = rs.getClob(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return clob;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object clob = cs.getClob(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return clob;
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
