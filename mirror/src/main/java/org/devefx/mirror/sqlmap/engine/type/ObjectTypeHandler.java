package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ObjectTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object o = rs.getObject(columnName);
		if (rs.wasNull())
			return null;
		return o;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object o = rs.getObject(columnIndex);
		if (rs.wasNull())
			return null;
		return o;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object o = cs.getObject(columnIndex);
		if (cs.wasNull())
			return null;
		return o;
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
