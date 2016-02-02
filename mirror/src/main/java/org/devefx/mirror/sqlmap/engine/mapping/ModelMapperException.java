package org.devefx.mirror.sqlmap.engine.mapping;

public class ModelMapperException extends RuntimeException {
	private static final long serialVersionUID = -5085588154711089622L;
	
	public ModelMapperException() {
	}
	public ModelMapperException(String message) {
		super(message);
	}
	public ModelMapperException(Throwable cause) {
		super(cause);
	}
	public ModelMapperException(String message, Throwable cause) {
		super(message, cause);
	}
}
