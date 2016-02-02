package org.devefx.mirror.sqlmap.engine.mapping.sql;

import java.util.HashMap;
import java.util.Map;

import org.devefx.mirror.sqlmap.engine.mapping.sql.builder.DeleteSqlBuilder;
import org.devefx.mirror.sqlmap.engine.mapping.sql.builder.InsertSqlBuilder;
import org.devefx.mirror.sqlmap.engine.mapping.sql.builder.SelectSqlBuilder;
import org.devefx.mirror.sqlmap.engine.mapping.sql.builder.UpdateSqlBuilder;

public class SqlBuilderFactory {
	
	private final Map<SqlBuilderType, SqlBuilder> sqlBuidlerMap = new HashMap<SqlBuilderType, SqlBuilder>();
	
	public SqlBuilderFactory() {
		sqlBuidlerMap.put(SqlBuilderType.SELECT, new SelectSqlBuilder());
		sqlBuidlerMap.put(SqlBuilderType.INSERT, new InsertSqlBuilder());
		sqlBuidlerMap.put(SqlBuilderType.UPDATE, new UpdateSqlBuilder());
		sqlBuidlerMap.put(SqlBuilderType.DELETE, new DeleteSqlBuilder());
	}
	
	public SqlBuilder getSqlBuilder(SqlBuilderType builderType) {
		return sqlBuidlerMap.get(builderType);
	}
}
