package org.devefx.mirror.sqlmap.engine.type;

public abstract class BaseTypeHandler implements TypeHandler {
	@Override
	public boolean equals(Object object, String string) {
		if (object == null || string == null)
			return object == null;
		return object.equals(valueOf(string));
	}
}
