package org.devefx.mirror.common.expression;

import java.lang.reflect.Method;

/**
 * Expression
 * @author： youqian.yue
 * @date： 2016-1-29 下午6:27:25
 */
public class Expression {
	
	public static Object getValue(String expression, Object root) throws ExpressionException {
		String[] tree = expression.split("\\.");
		int len = tree.length;
		if (len == 1) {
			return invoke(root, expression, "get");
		} else {
			for (int i = 0; i < len; i++) {
				root = invoke(root, tree[i], "get");
				if (root == null && i + 1 < len) {
					throw new ExpressionException("source is null for '" + tree[i] + "'");
				}
			}
			return root;
		}
	}

	public static void setValue(String expression, Object root, Object value) throws ExpressionException {
		String[] tree = expression.split("\\.");
		int len = tree.length;
		if (len == 1) {
			invoke(root, expression, "set", value);
		} else {
			Object memory = null;
			Object cursor = root;
			Class<?> clazz = root.getClass();
			for (int i = 0; i < len; i++) {
				if (i + 1 < len) {
					cursor = invoke(cursor, tree[i], "get");
					if (cursor == null) {
						try {
							clazz = clazz.getDeclaredField(tree[i]).getType();
							cursor = clazz.newInstance();
						} catch (NoSuchFieldException e) {
							throw new ExpressionException("Could not found field '"+ tree[i] +"' of class [" + clazz.getName() + "]. ");
						} catch (Exception e) {
							throw new ExpressionException("Could not instantiate class [" + clazz.getName() + "].  Cause: No default constructor found. ");
						}
						if (memory == null) {
							memory = cursor;
							continue;
						}
						invoke(memory, tree[i], "set", cursor);
					}
				} else {
					invoke(memory, tree[len - 1], "set", value);
				}
			}
			invoke(root, tree[0], "set", memory);
		}
	}
	
	public static Object invoke(Object object, String name, String methodType, Object ...value) throws ExpressionException {
		Class<?> clazz = object.getClass();
		Method method = null;
		String methodName = methodType + name.substring(0, 1).toUpperCase();
		if (name.length() > 1) {
			methodName += name.substring(1);
		}
		if ("set".equals(methodType)) {
			Class<?> parameterType = null;
			try {
				parameterType = clazz.getDeclaredField(name).getType();
				method = clazz.getMethod(methodName, parameterType);
				return method.invoke(object, value);
			} catch (NoSuchFieldException e) {
				throw new ExpressionException("Could not found field '"+ name +"' of class [" + clazz.getName() + "].");
			} catch (NoSuchMethodException e) {
				throw new ExpressionException("Could not found method '"+ clazz.getName() + "." + methodName + "(" + parameterType.getName() + ")" + "'.");
			} catch (Exception e) {
				throw new ExpressionException("invoke method ['" + method.getName() + "'] fail.");
			}
		}
		try {
			method = clazz.getMethod(methodName);
			return method.invoke(object, value);
		} catch (NoSuchMethodException e) {
			throw new ExpressionException("Could not found method '"+ methodName +"' of class [" + clazz.getName() + "].");
		} catch (Exception e) {
			throw new ExpressionException("invoke method ['" + method.getName() + "'] fail.");
		}
	}
	
}
