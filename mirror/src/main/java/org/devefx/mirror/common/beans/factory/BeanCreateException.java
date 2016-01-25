package org.devefx.mirror.common.beans.factory;

public class BeanCreateException extends Exception {
	
	private static final long serialVersionUID = -4304866778229027499L;
	
	public BeanCreateException() {
	}
	public BeanCreateException(String message) {
		super(message);
	}
	public BeanCreateException(Throwable cause) {
		super(cause);
	}
	public BeanCreateException(String message, Throwable cause) {
		super(message, cause);
	}
}
