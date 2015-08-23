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

package com.mihaila.zutil.text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.mihaila.zutil.lang.WrappedInRuntimeException;


/**
 * String SQL escaping functions.
 */
public class EncodeUtil {

	// Replacements in XML/HTML documents for " & < > ' characters

	public static final String CHAR_ENTITY_QUOT = "&quot;";

	public static final String CHAR_ENTITY_AMP = "&amp;";

	public static final String CHAR_ENTITY_LT = "&lt;";

	public static final String CHAR_ENTITY_GT = "&gt;";

	public static final String CHAR_ENTITY_APOS = "&apos;";

	public static final String CHAR_ENTITY_APOS_HTML = "&#29;";

	private static final String[] HTML_ENTITIES = { CHAR_ENTITY_QUOT,
			CHAR_ENTITY_AMP, CHAR_ENTITY_LT, CHAR_ENTITY_GT,
			CHAR_ENTITY_APOS_HTML };

	private static final String[] XML_ENTITIES = { CHAR_ENTITY_QUOT,
			CHAR_ENTITY_AMP, CHAR_ENTITY_LT, CHAR_ENTITY_GT, CHAR_ENTITY_APOS };

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * it to an SQL query (turns single-quotes(') into doubled single-quotes
	 * ('')). Example: "it's raining" becomes "'it''s raining'".
	 * 
	 * @param value
	 *            the string to be appended
	 * @return the escaped string
	 */
	public static String sqlEncode(String value) {
		if (value == null) {
			return "NULL";
		}
		if (value.indexOf('\'') == -1) {
			return "'" + value + '\'';
		} else {
			// 12.5% percent increase estimation, minimum 8
			int estimatedSize = value.length() * 9 / 8;
			if (value.length() < 8) {
				estimatedSize = 8;
			}
			StringBuilder result = new StringBuilder(estimatedSize);
			writeSqlEncoded(result, value);
			return result.toString();
		}
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * to an SQL query (turns single-quotes(') into doubled single-quotes ('')).
	 * An object implementing <code>Appendable</code> is used as output for
	 * performance reasons. Example: "it's raining" becomes "'it''s raining'".
	 * 
	 * @param out
	 *            the Appendable where to append the result
	 * @param value
	 *            the string to be appended
	 */
	public static void writeSqlEncoded(Appendable out, String value) {
		try {
			if (value == null) {
				out.append("NULL");
				return;
			}
			out.append('\'');
			int start = 0;
			int end = value.indexOf('\'');
			if (end == -1) {
				out.append(value);
			} else {
				while (end != -1) {
					out.append(value.substring(start, end + 1));
					out.append('\'');
					start = end + 1;
					end = value.indexOf('\'', start);
				}
				out.append(value.substring(start));
			}
			out.append('\'');
		} catch (IOException e) {
			throw new WrappedInRuntimeException(e);
		}
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to use
	 * it in a URL.
	 * 
	 * @param value
	 * @return the escaped string
	 */
	public static String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new WrappedInRuntimeException(e);
		}
	}

	/**
	 * The characters: " & < > are escaped with their correspondent character
	 * entities. "'" is replaced by .
	 * 
	 * @param c
	 *            the character to be escaped
	 */
	public static String writeHtmlEncoded(char c) {
		int entityIndex = getCharEntityIndex(c);
		if (entityIndex == -1) {
			return String.valueOf(c);
		} else {
			return HTML_ENTITIES[entityIndex];
		}
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * it in an HTML document. The characters: " & < > are escaped with their
	 * correspondent character entities. "'" is replaced by &#29;.
	 * 
	 * @param value
	 *            the string to be appended
	 * @return the escaped string
	 */
	public static String htmlEncode(String value) {
		return escapeUsingEntities(value, HTML_ENTITIES);
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * it in an HTML document. The characters: " & < > are escaped with their
	 * correspondent character entities. "'" is replaced by &#29;. The result is
	 * appended to the specified <code>Appendable</code> object.
	 * 
	 * @param out
	 *            the Appendable where to append the result
	 * @param value
	 *            the string to be appended
	 */
	public static void writeHtmlEncoded(Appendable out, String value) {
		escapeUsingEntities(out, value, HTML_ENTITIES);
	}

	/**
	 * Returns true if the specified char is an must be escaped if used in a
	 * HTML document, false otherwise. The method returns true for: " & < > '
	 * 
	 * @param c
	 * @return true if the specified char is an must be escaped if used in a
	 *         HTML document, false otherwise
	 */
	public static boolean mustEscapeCharInHtml(char c) {
		return (getCharEntityIndex(c) == -1);
	}

	/**
	 * The characters: " & < > ' are escaped with their correspondent character
	 * entities.
	 * 
	 * @param c
	 *            the character to be escaped
	 */
	public static String xmlEncode(char c) {
		int entityIndex = getCharEntityIndex(c);
		if (entityIndex == -1) {
			return String.valueOf(c);
		} else {
			return XML_ENTITIES[entityIndex];
		}
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * it in an XML document. The characters: " & < > ' are escaped with their
	 * correspondent character entities.
	 * 
	 * @param value
	 *            the string to be appended
	 * @return the escaped string
	 */
	public static String xmlEncode(String value) {
		return escapeUsingEntities(value, XML_ENTITIES);
	}

	/**
	 * Escapes the characters in a <code>String</code> to be suitable to pass
	 * it in an XML document. The characters: " & < > ' are escaped with their
	 * correspondent character entities. The result is appended to the specified
	 * <code>Appendable</code> object.
	 * 
	 * @param out
	 *            the Appendable where to append the result
	 * @param value
	 *            the string to be appended
	 */
	public static void writeXmlEncoded(Appendable out, String value) {
		escapeUsingEntities(out, value, XML_ENTITIES);
	}

	/**
	 * Returns true if the specified char is an must be escaped if used in a XML
	 * document, false otherwise. The method returns true for: " & < > '
	 * 
	 * @param c
	 * @return true if the specified char is an must be escaped if used in a XML
	 *         document, false otherwise
	 */
	public static boolean mustEscapeCharInXml(char c) {
		return (getCharEntityIndex(c) == -1);
	}

	/**
	 * Returns an index in HTML/XML_ENTITIES tables for the specified char if it
	 * has a corresponding character entity, -1 otherwise.
	 * 
	 * @param c
	 * @return an index in HTML/XML_ENTITIES tables for the specified char if it
	 *         has a corresponding character entity, -1 otherwise
	 */
	private static int getCharEntityIndex(char c) {
		if (c > 62) {
			return -1;
		} else {
			switch (c) {
			case '"':
				return 0;
			case '&':
				return 1;
			case '<':
				return 2;
			case '>':
				return 3;
			case '\'':
				return 4;
			default:
				return -1;
			}
		}
	}

	/**
	 * Helper method used in <code>escapeHtml(String)</code> and
	 * <code>escapeXml(String)</code>.
	 * 
	 * @param value
	 * @param entities
	 * @return
	 */
	private static String escapeUsingEntities(String value, String[] entities) {
		if (value == null) {
			return "";
		}
		int i;
		for (i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (getCharEntityIndex(c) != -1) {
				break;
			}
		}
		if (i == value.length()) {
			return value;
		}
		// 12.5% percent increase estimation, minimum 8
		int estimatedSize = value.length() * 9 / 8;
		if (value.length() < 8) {
			estimatedSize = 8;
		}
		StringBuilder out = new StringBuilder(estimatedSize);
		out.append(value.substring(0, i));
		escapeUsingEntities(out, value.substring(i), entities);
		return out.toString();
	}

	/**
	 * Helper method used in <code>escapeHtml(Appendable, String)</code> and
	 * <code>escapeXml(Appendable, String)</code>.
	 * 
	 * @param value
	 * @param entities
	 * @return
	 */
	private static void escapeUsingEntities(Appendable out, String value,
			String[] entities) {
		if (value == null) {
			return;
		}
		try {
			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				int entityIndex = getCharEntityIndex(c);
				if (entityIndex == -1) {
					out.append(c);
				} else {
					out.append(entities[entityIndex]);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
