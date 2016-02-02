package org.devefx.mirror.common.expression;

public class ExpressionException extends Exception {
	
	private static final long serialVersionUID = -2787895328007458541L;
	
	public ExpressionException() {
		super();
	}
	public ExpressionException(String msg) {
		super(msg);
	}
	public ExpressionException(Throwable cause) {
		super(cause);
	}
	public ExpressionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
