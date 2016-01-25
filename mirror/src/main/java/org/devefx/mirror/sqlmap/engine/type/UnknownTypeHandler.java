package org.devefx.mirror.sqlmap.engine.type;


public class UnknownTypeHandler extends ObjectTypeHandler {

	private TypeHandlerFactory factory;
	
	public UnknownTypeHandler(TypeHandlerFactory factory) {
		this.factory = factory;
	}

	@Override
	public boolean equals(Object object, String string) {
		if (object == null || string == null)
			return object == string;
		TypeHandler handler = factory.getTypeHandler(object.getClass());
		return object.equals(handler.valueOf(string));
	}
}
