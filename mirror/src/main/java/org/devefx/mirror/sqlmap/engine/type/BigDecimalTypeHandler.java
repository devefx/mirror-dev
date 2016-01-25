package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BigDecimalTypeHandler extends BaseTypeHandler implements TypeHandler {

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		Object bigdec = rs.getBigDecimal(columnName);
		if (rs.wasNull())
			return null;
		return bigdec;
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object bigdec = rs.getBigDecimal(columnIndex);
		if (rs.wasNull())
			return null;
		return bigdec;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object bigdec = cs.getBigDecimal(columnIndex);
		if (cs.wasNull())
			return null;
		return bigdec;
	}

	@Override
	public Object valueOf(String s) {
		return java.math.BigDecimal.valueOf(Long.valueOf(s).longValue());
	}
}
