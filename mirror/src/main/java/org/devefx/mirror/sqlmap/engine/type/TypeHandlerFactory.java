package org.devefx.mirror.sqlmap.engine.type;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"serial", "rawtypes"})
public class TypeHandlerFactory {
	
	private final Map<Class, TypeHandler> typeHandlerMap = new HashMap<Class, TypeHandler>();
	private final TypeHandler unknownTypeHandler = new UnknownTypeHandler(this);
	
	private static final Map<Class, Class> reversePrimitiveMap = new HashMap<Class, Class>() {{
		put(Byte.class, byte.class);
		put(Short.class, short.class);
		put(Integer.class, int.class);
		put(Long.class, long.class);
		put(Float.class, float.class);
		put(Double.class, double.class);
		put(Boolean.class, boolean.class);
	}};
	
	public TypeHandlerFactory() {
		
		register(Byte.class, new ByteTypeHandler());
		register(Short.class, new ShortTypeHandler());
		register(Integer.class, new IntegerTypeHandler());
		register(Long.class, new LongTypeHandler());
		register(Float.class, new FloatTypeHandler());
		register(Double.class, new DoubleTypeHandler());
		register(Boolean.class, new BooleanTypeHandler());
		
		register(String.class, new StringTypeHandler());
		register(BigDecimal.class, new BigDecimalTypeHandler());
		register(byte[].class, new ByteArrayTypeHandler());
		register(Byte[].class, new ByteArrayTypeHandler());
		register(Object.class, new ObjectTypeHandler());
		register(URL.class, new URLTypeHandler());
		register(Date.class, new DateTypeHandler());
		
		register(java.sql.Blob.class, new BlobTypeHandler());
		register(java.sql.Clob.class, new ClobTypeHandler());
		
		register(java.sql.Date.class, new SqlDateTypeHandler());
		register(java.sql.Time.class, new SqlTimeTypeHandler());
		register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());
		
	}

	public void register(Class type, TypeHandler handler) {
		typeHandlerMap.put(type, handler);
		if (reversePrimitiveMap.containsKey(type)) {
			register(reversePrimitiveMap.get(type), handler);
		}
	}

	public TypeHandler getTypeHandler(Class type) {
		TypeHandler handler = typeHandlerMap.get(type);
		if (handler == null && type != null && Enum.class.isAssignableFrom(type)) {
			handler = new EnumTypeHandler(type);
		}
		return handler;
	}
	
	public TypeHandler getUnkownTypeHandler() {
		return unknownTypeHandler;
	}
	
	public boolean hasTypeHandler(Class type) {
		return type != null && (getTypeHandler(type) != null || Enum.class.isAssignableFrom(type));
	}
}
