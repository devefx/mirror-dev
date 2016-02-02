package org.devefx.mirror.sqlmap.engine.model.member;

public interface Member {
	
	public String getName();
	
	public Class<?> getType();
	
	public boolean isCollection();
}
