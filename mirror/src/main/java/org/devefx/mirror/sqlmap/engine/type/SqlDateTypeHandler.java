package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlDateTypeHandler extends BaseTypeHandler implements TypeHandler {

	private static final String DATE_FORMAT = "yyyy/MM/dd";
	
	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object sqlDate = rs.getDate(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return sqlDate;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object sqlDate = rs.getDate(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return sqlDate;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object sqlDate = cs.getDate(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return sqlDate;
	}

	@Override
	public Object valueOf(String s) {
		return SimpleDateFormatter.format(DATE_FORMAT, s);
	}
}
