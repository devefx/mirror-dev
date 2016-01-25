package org.devefx.mirror.common.xml;

import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeletUtils {
	
	public static Properties parseAttributes(Node n, Properties variables) {
		Properties attributes = new Properties();
		NamedNodeMap attributeNodes = n.getAttributes();
		for (int i = 0; i < attributeNodes.getLength(); i++) {
			Node attribute = attributeNodes.item(i);
			String value = parsePropertyTokens(attribute.getNodeValue(), variables);
			attributes.put(attribute.getNodeName(), value);
		}
		return attributes;
	}
	
	public static String parsePropertyTokens(String string, Properties variables) {
		final String OPEN = "${";
		final String CLOSE = "}";
		String newString = string;
		
		if (string != null && variables != null) {
			int start = newString.indexOf(OPEN);
			int end = newString.indexOf(CLOSE, start);
			
			while (start > -1 && end > start) {
				String prepend = newString.substring(0, start);
				String append = newString.substring(end + CLOSE.length());
				String propName = newString.substring(start + OPEN.length(), end);
				Object propValue = variables.get(propName);
				if (propValue == null) {
					newString = prepend + propName + append;
				} else {
					newString = prepend + propValue + append;
				}
				start = newString.indexOf(OPEN);
				end = newString.indexOf(CLOSE, start);
			}
		}
		return newString;
	}
}
