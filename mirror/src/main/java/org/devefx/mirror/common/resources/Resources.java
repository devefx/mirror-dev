package org.devefx.mirror.common.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class Resources {
	
	private static ClassLoader defaultClassLoader;

	public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		Resources.defaultClassLoader = defaultClassLoader;
	}
	
	public static URL getResourceURL(String resource) throws IOException {
		return getResourceURL(getClassLoader(), resource);
	}
	
	public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
		URL url = null;
		if (loader != null) url = loader.getResource(resource);
		if (url == null) url = ClassLoader.getSystemResource(resource);
		if (url == null) throw new IOException("Could not find resource " + resource);
		return url;
	}
	
	public static InputStream getResourceAsStream(String resource) throws IOException {
		return getResourceAsStream(getClassLoader(), resource);
	}
	
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
		InputStream in = null;
		if (loader != null) in = loader.getResourceAsStream(resource);
		if (in == null) in = ClassLoader.getSystemResourceAsStream(resource);
		if (in == null) throw new IOException("Could not find resource " + resource);
		return in;
	}
	
	public static Properties getResourceAsProperties(String resource) throws IOException {
		return getResourceAsProperties(getClassLoader(), resource);
	}
	
	public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = getResourceAsStream(loader, resource);
			props.load(in);
		} finally {
			in.close();
		}
		return props;
	}
	
	public static InputStream getUrlAsStream(String urlString) throws IOException {
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		return conn.getInputStream();
	}
	
	public static Properties getUrlAsProperties(String urlString) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = getUrlAsStream(urlString);
			props.load(in);
		} finally {
			in.close();
		}
		return props;
	}
	
	public static Class<?> classForName(String className) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			clazz = getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			clazz = Class.forName(className);
		}
		return clazz;
	}
	
	public static Object instantiate(String className) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return instantiate(classForName(className));
	}
	
	public static Object instantiate(Class<?> clazz) throws InstantiationException,
			IllegalAccessException {
		return clazz.newInstance();
	}
	
	public static ClassLoader getClassLoader() {
		if (defaultClassLoader != null)
			return defaultClassLoader;
		return Thread.currentThread().getContextClassLoader();
	}
	
}
