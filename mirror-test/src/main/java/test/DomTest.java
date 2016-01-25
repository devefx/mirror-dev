package test;

import java.io.IOException;
import java.io.InputStream;

import org.devefx.mirror.common.resources.Resources;
import org.devefx.mirror.sqlmap.engine.builder.xml.SqlMapConfigParser;

public class DomTest {
	
	public static void main(String[] args) throws IOException {
		InputStream is = Resources.getResourceAsStream("mirror.xml");
		try {
			SqlMapConfigParser parser = new SqlMapConfigParser();
			parser.parse(is);
			
			System.out.println(parser);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
}
