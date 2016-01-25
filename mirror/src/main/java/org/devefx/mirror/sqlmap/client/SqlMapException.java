package org.devefx.mirror.sqlmap.client;

public class SqlMapException extends RuntimeException {
	private static final long serialVersionUID = 2539247293699787608L;
	
	public SqlMapException() {
	}
	public SqlMapException(String message) {
		super(message);
	}
	public SqlMapException(Throwable cause) {
		super(cause);
	}
	public SqlMapException(String message, Throwable cause) {
		super(message, cause);
	}
}
