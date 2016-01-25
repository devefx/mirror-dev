package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class DateTypeHandler extends BaseTypeHandler implements TypeHandler {

	private static final String DATE_FORMAT = "yyyy/MM/dd hh:mm:ss";
	
	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Timestamp time = rs.getTimestamp(columnName);
		if (rs.wasNull())
			return null;
		return new Date(time.getTime());
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Timestamp time = rs.getTimestamp(columnIndex);
		if (rs.wasNull())
			return null;
		return new Date(time.getTime());
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Timestamp time = cs.getTimestamp(columnIndex);
		if (cs.wasNull())
			return null;
		return new Date(time.getTime());
	}

	@Override
	public Object valueOf(String s) {
		return SimpleDateFormatter.format(DATE_FORMAT, s);
	}
}
