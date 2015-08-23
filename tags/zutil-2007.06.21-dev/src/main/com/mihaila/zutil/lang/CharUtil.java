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

package com.mihaila.zutil.lang;

/**
 * Various functions related to character processing (in addition to the methods
 * found in <code>java.lang.Character</code> and
 * <code>org.apache.commons.lang.CharUtils</code>).
 */
public class CharUtil {

	/**
	 * Converts the specified character to upper case if it is a valid latin
	 * letter, otherwise the same character is returned.
	 * 
	 * @param ch
	 * @return the upper cased char
	 */
	public static char toAsciiUpperCase(char ch) {
		if ((ch >= 'a') && (ch <= 'z')) {
			return (char) (ch + 'A' - 'a');
		} else {
			return ch;
		}
	}

	/**
	 * Converts the specified character to lower case if it is a valid latin
	 * letter, otherwise the same character is returned.
	 * 
	 * @param ch
	 * @return the upper cased char
	 */
	public static char toAsciiLowerCase(char ch) {
		if ((ch >= 'A') && (ch <= 'Z')) {
			return (char) (ch + 'a' - 'A');
		} else {
			return ch;
		}
	}
}
