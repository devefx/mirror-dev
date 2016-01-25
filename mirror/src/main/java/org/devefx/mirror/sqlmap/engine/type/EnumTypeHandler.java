package org.devefx.mirror.sqlmap.engine.type;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@SuppressWarnings({"rawtypes", "unchecked"})
public class EnumTypeHandler extends BaseTypeHandler implements TypeHandler {
	
	private Class enumType;
	
	public EnumTypeHandler(Class enumType) {
		this.enumType = enumType;
	}
	
	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		String s = rs.getString(columnName);
		if (rs.wasNull())
			return null;
		return Enum.valueOf(enumType, s);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
		if (rs.wasNull())
			return null;
		return Enum.valueOf(enumType, s);
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String s = cs.getString(columnIndex);
		if (cs.wasNull())
			return null;
		return Enum.valueOf(enumType, s);
	}

	@Override
	public Object valueOf(String s) {
		return Enum.valueOf(enumType, s);
	}

}
