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

public class Debug {

	public static final boolean ENABLED;

	private static String DEBUG_FLAG_APP_PROPERTY = "app.debug";

	static {
		boolean enabled = false;
		String s = ApplicationProperties.get().getProperty(
				DEBUG_FLAG_APP_PROPERTY);
		if (s != null) {
			s = s.trim();
			if ("true".equals(s)) {
				enabled = true;
			} else if ("false".equals(s)) {
				enabled = false;
			} else {
				throw new RuntimeException(
						DEBUG_FLAG_APP_PROPERTY
								+ " application property was set to an invalid value: " + s + " (must be \"true\" or \"false\")");
			}
		}
		ENABLED = enabled;
	}
}
