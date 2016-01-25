package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BlobTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object blob = rs.getBlob(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return blob;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object blob = rs.getBlob(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return blob;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object blob = cs.getBlob(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return blob;
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}

}
