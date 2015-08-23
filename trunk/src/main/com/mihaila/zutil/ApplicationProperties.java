/*
 * Copyright (c) 2007 Cornel Mihaila (http://www.mihaila.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.package zutil;
 */

package com.mihaila.zutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.mihaila.zutil.lang.WrappedInRuntimeException;


/**
 * Replacement for the <code>Properties</code> object returned by
 * <code>System.getProperties</code>.
 * <code>ApplicationProperties.get()</code> returns a <code>Properties</code>
 * object that has the system properties as default entries, and is initialized
 * using application.properties or application.xml files.
 */
public class ApplicationProperties {

	/**
	 * Returns the application properties.
	 * 
	 * @return the application properties
	 */
	public static Properties get() {
		return m_properties;
	}

	/**
	 * Name of the properties file containing application properties.
	 */
	private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";

	/**
	 * Name of the xml file containing application properties.
	 */
	private static final String APPLICATION_XML_FILENAME = "application.xml";

	/**
	 * Contains the application properties.
	 */
	private static Properties m_properties = new Properties(System
			.getProperties());

	static {
		// load additional properties from application.properties or
		// application.xml filenames.
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		FileInputStream in = null;
		boolean xmlFile = false;
		URL url = classLoader.getResource(APPLICATION_PROPERTIES_FILENAME);
		if (url == null) {
			xmlFile = true;
			url = classLoader.getResource(APPLICATION_XML_FILENAME);
		}
		if (url != null) {
			try {
				in = new FileInputStream(url.getFile());
				if (xmlFile) {
					m_properties.loadFromXML(in);
				} else {
					m_properties.load(in);
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				throw new WrappedInRuntimeException(e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					throw new WrappedInRuntimeException(e);
				}
			}
		}
	}

}
