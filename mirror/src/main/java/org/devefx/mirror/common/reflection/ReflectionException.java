package org.devefx.mirror.common.reflection;

public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = 2378604393125885383L;

	public ReflectionException() {
		super();
	}
	public ReflectionException(String msg) {
		super(msg);
	}
	public ReflectionException(Throwable cause) {
		super(cause);
	}
	public ReflectionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
