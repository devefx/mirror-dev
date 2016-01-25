package org.devefx.mirror.sqlmap.engine.type;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class URLTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object url = rs.getURL(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return url;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object url = rs.getURL(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		return url;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object url = cs.getURL(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		return url;
	}

	@Override
	public Object valueOf(String s) {
		try {
			return new URL(s);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
