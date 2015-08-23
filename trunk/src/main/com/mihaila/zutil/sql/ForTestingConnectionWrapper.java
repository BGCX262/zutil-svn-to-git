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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ConnectionWrapper that implementds 2 features usefull in testcases:
 *  - make commit behave as rollback
 *  - count the number of created statements
 */
public class ForTestingConnectionWrapper extends ConnectionWrapper {

	/**
	 * @param connection
	 *            the wrapped connection
	 */
	public ForTestingConnectionWrapper(Connection connection) {
		super(connection);
	}

	/**
	 * @return true, if rollbackOnCommit mode is ON, false otherwise
	 */
	public boolean getRollbackOnCommit() {
		return m_rollbackOnCommit;
	}

	/**
	 * If set to true, <code>Connection.commit()</code> will be equivalent
	 * with <code>Connection.rollback()</code>. If false, then commit() will
	 * function as usual.
	 * 
	 * @param value
	 */
	public void setRollbackOnCommit(boolean value) {
		m_rollbackOnCommit = value;
	}

	/**
	 * Returns the number of created statements (with
	 * <code>createStatement()</code>).
	 * 
	 * @return the number of created statements (with
	 *         <code>createStatement()</code>).
	 */
	public int getNumCreatedStatements() {
		return m_numCreatedStatements;
	}

	/**
	 * Clears the counter that holds number of created statements.
	 */
	public void clearNumCreatedStatements() {
		m_numCreatedStatements = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zutil.sql.GenericConnectionWrapper#commit()
	 */
	@Override
	public void commit() throws SQLException {
		if (m_rollbackOnCommit) {
			rollback();
		} else {
			super.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zutil.sql.GenericConnectionWrapper#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		m_numCreatedStatements++;
		return super.createStatement();
	}

	/**
	 * If true, <code>Connection.commit()</code> will be equivalent with
	 * <code>Connection.rollback()</code>
	 */
	private boolean m_rollbackOnCommit = false;

	/**
	 * Holds the number of created statements (with
	 * <code>createStatement()</code>)
	 */
	private int m_numCreatedStatements;
}
