package org.devefx.mirror.sqlmap.engine.mapping.sql;

import org.devefx.mirror.sqlmap.engine.mapping.ModelMapper;

public interface SqlBuilder {
	
	Sql builder(ModelMapper mapper);
}
