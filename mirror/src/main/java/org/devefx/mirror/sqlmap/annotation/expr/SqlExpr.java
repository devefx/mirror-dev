package org.devefx.mirror.sqlmap.annotation.expr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlExpr {
	/** general */
	String expr() default "";
	String join() default "";
	/** field */
	Class<?>[] modelClass() default {};
	/** foreach */
	String foreach() default "";
	String item() default "";
	/** if */
	String ifnull() default "";
	String ifnotnull() default "";
}
