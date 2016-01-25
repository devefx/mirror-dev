package org.devefx.mirror.common.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.devefx.mirror.common.resources.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class NodeletParser {
	private static final String MIRROR_XSD = "org/devefx/mirror/sqlmap/engine/builder/xml/mirror-1.0.xsd";
	
	private Map<String, Nodelet> letMap = new HashMap<String, Nodelet>();
	
	public void addNodelet(String path, Nodelet nodelet) {
		letMap.put(path, nodelet);
	}
	
	public void parse(Reader reader) throws NodeletException {
		try {
			Document doc = createDocument(new InputSource(reader));
			parse(doc.getLastChild());
		} catch (Exception e) {
			throw new NodeletException("Error parsing XML.  Cause: " + e, e);
		}
	}
	
	public void parse(InputStream inputStream) throws NodeletException {
		try {
			Document doc = createDocument(new InputSource(inputStream));
			parse(doc.getLastChild());
		} catch (Exception e) {
			throw new NodeletException("Error parsing XML.  Cause: " + e, e);
		}
	}
	
	private Document createDocument(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		
		factory.setNamespaceAware(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setCoalescing(false);
		factory.setExpandEntityReferences(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(inputSource);
		
		// schema validate
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(Resources.getResourceURL(MIRROR_XSD));
		Validator validator =  schema.newValidator();
		validator.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException exception) throws SAXException {
			}
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				throw exception;
			}
			@Override
			public void error(SAXParseException exception) throws SAXException {
				throw exception;
			}
		});
		validator.validate(new DOMSource(document));
		return document;
	}
	
	private void parse(Node node) {
		Path path = new Path();
		processNodelet(node, "/");
		process(node, path);
	}
	
	private void process(Node node, Path path) {
		if (node instanceof Element) {
			// Element
			String elementName = node.getNodeName();
			path.add(elementName);
			processNodelet(node, path.toString());
			processNodelet(node, new StringBuffer("//").append(elementName).toString());
			// Attribule
			NamedNodeMap attributes = node.getAttributes();
			for (int i = 0, n = attributes.getLength(); i < n; i++) {
				Node att = attributes.item(i);
				String attrName = att.getNodeName();
				path.add("@" + attrName);
				processNodelet(att, path.toString());
		        processNodelet(node, new StringBuffer("//@").append(attrName).toString());
				path.remove();
			}
			// Children
			NodeList children = node.getChildNodes();
			for (int i = 0, n = children.getLength(); i < n; i++) {
				process(children.item(i), path);
			}
			path.add("end()");
			processNodelet(node, path.toString());
			path.remove();
			path.remove();
			
		} else if (node instanceof Text) {
			// Text
			path.add("text()");
			processNodelet(node, path.toString());
			processNodelet(node, "//text()");
			path.remove();
		}
	}
	private void processNodelet(Node node, String pathString) {
		Nodelet nodelet = letMap.get(pathString);
		if (nodelet != null) {
			try {
				nodelet.process(node);
			} catch (Exception e) {
				throw new RuntimeException("Error parsing XPath '" + pathString + "'.  Cause: " + e, e);
			}
		}
		//System.out.println(pathString);
	}
	
	private static class Path {
		
		private List<String> nodeList = new ArrayList<String>();
		
		public void add(String node) {
			nodeList.add(node);
		}
		public void remove() {
			nodeList.remove(nodeList.size() - 1);
		}
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer("/");
			for (int i = 0, n = nodeList.size(); i < n; i++) {
				buffer.append(nodeList.get(i));
				if (i < n - 1) {
					buffer.append("/");
				}
			}
			return buffer.toString();
		}
	}
}
