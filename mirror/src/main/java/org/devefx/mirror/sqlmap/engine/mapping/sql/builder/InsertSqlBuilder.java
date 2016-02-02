package org.devefx.mirror.sqlmap.engine.mapping.sql.builder;

import java.util.Iterator;

import org.devefx.mirror.sqlmap.engine.mapping.ModelMapper;
import org.devefx.mirror.sqlmap.engine.mapping.sql.Sql;
import org.devefx.mirror.sqlmap.engine.mapping.sql.SqlBuilder;

public class InsertSqlBuilder implements SqlBuilder {

	@Override
	public Sql builder(ModelMapper mapper) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append(mapper.getTableName());
		sql.append("(");
		
		Iterator<String> it = mapper.getMappedColumnNames();
		while (it.hasNext()) {
			sql.append(it.next());
			sql.append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		
		sql.append(") values(");
		
		int len = mapper.getMappedColumn().size();
		for (int i = 0; i < len; i++) {
			sql.append("?");
			if (i + 1 != len) {
				sql.append(", ");
			}
		}
		
		sql.append(")");
		
		Sql sql2 = new Sql();
		sql2.setStaticSql(sql.toString());
		return sql2;
	}

}
