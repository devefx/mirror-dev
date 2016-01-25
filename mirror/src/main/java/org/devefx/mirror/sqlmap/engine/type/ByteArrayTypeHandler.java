package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ByteArrayTypeHandler extends BaseTypeHandler implements
		TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object bytes = rs.getBytes(columnName);
		if (rs.wasNull())
			return null;
		return bytes;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object bytes = rs.getBytes(columnIndex);
		if (rs.wasNull())
			return null;
		return bytes;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object bytes = cs.getBytes(columnIndex);
		if (cs.wasNull())
			return null;
		return bytes;
	}

	@Override
	public Object valueOf(String s) {
		return s.getBytes();
	}
}
