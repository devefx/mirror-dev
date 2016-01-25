package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlTimeTypeHandler extends BaseTypeHandler implements TypeHandler {

	private static final String DATE_FORMAT = "hh:mm:ss";
	
	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object time = rs.getTime(columnName);
		if (rs.wasNull())
			return null;
		return time;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object time = rs.getTime(columnIndex);
		if (rs.wasNull())
			return null;
		return time;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object time = cs.getTime(columnIndex);
		if (cs.wasNull())
			return null;
		return time;
	}

	@Override
	public Object valueOf(String s) {
		return SimpleDateFormatter.format(DATE_FORMAT, s);
	}
}
