package org.devefx.mirror.sqlmap.engine.type;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.devefx.mirror.sqlmap.client.SqlMapException;

public class SimpleDateFormatter {
	public static Date format(String format, String datetime) {
		try {
			return new SimpleDateFormat(format).parse(datetime);
		} catch (Exception e) {
			throw new SqlMapException("Error parsing default null value date.  Format must be '" + format + "'. Cause: " + e);
		}
	}
}
