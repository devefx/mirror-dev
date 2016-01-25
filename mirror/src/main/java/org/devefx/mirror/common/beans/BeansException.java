package org.devefx.mirror.common.beans;

public class BeansException extends RuntimeException {

	private static final long serialVersionUID = 127878922735093319L;
	
	public BeansException() {
	}
	public BeansException(String message) {
		super(message);
	}
	public BeansException(Throwable cause) {
		super(cause);
	}
	public BeansException(String message, Throwable cause) {
		super(message, cause);
	}
}
