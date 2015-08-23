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

package com.mihaila.zutil.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Facilitates testcases working with databases.
 * 
 */
public class ConnectionManager {

	public final static String PROPERTIES_FILE = "db.properties";

	public ConnectionManager() {
		configureDriverManager();
	}

	/**
	 * Configure the DriverManager by using the db.properties file to configure
	 * the DriverManger (driverName, jdbcUrl, user and password). This function
	 * is automatically called in the constructor. You can overwrite this
	 * function if you want to use a different wait of configuration
	 * 
	 */
	public void configureDriverManager() {
		try {
			InputStream is = ConnectionManager.class.getClassLoader()
					.getResourceAsStream(PROPERTIES_FILE);

			Properties properties = new Properties();
			properties.load(is);
			is.close();
			m_driverName = properties.getProperty("db.driverName");
			m_jdbcUrl = properties.getProperty("db.jdbcUrl");
			m_user = properties.getProperty("db.user");
			m_password = properties.getProperty("db.password");

		} catch (Exception e) {
			throw new RuntimeException("Error loading property file: "
					+ PROPERTIES_FILE, e);
		}

		// only for debugging
		// System.out.println("driverName: " + m_driverName);
		// System.out.println("jdbcUrl: " + m_jdbcUrl);
		// System.out.println("user: " + m_user);
		// System.out.println("password: " + m_password);

		try {
			Class.forName(m_driverName);
		} catch (Exception e) {
			throw new RuntimeException(
					"JDBC driver not found: " + m_driverName, e);
		}
	}

	/**
	 * Returns the connection to database.
	 * 
	 * @return the conection to database
	 */
	public Connection getConnection() {
		Connection conn;
		try {
			conn = DriverManager.getConnection(m_jdbcUrl, m_user, m_password);
		} catch (Exception e) {
			throw new RuntimeException("Error getting a Connection", e);
		}
		return conn;
	}

	String m_driverName;

	String m_jdbcUrl;

	String m_user;

	String m_password;
}
