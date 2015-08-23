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

package com.mihaila.zutiltest.text;

import java.io.StringWriter;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mihaila.zutil.text.EncodeUtil;
import com.mihaila.zutil.text.HtmlEncodeWriter;


/**
 * Tests <code>zutil.text.StringEscapeUtils</code> and also
 * code>zutil.text.HtmlEscapeWriter</code>
 */
public class EncodeUtilTest {

	@DataProvider(name = "escapeSql")
	public Object[][] sqlEncodeData() {
		return new Object[][] { new Object[] { "no single quote",
				"'no single quote'" } };
	}

	@Test
	public void testSqlEncode() {
		// test escapeHtml(char) methods
		Assert.assertSame(EncodeUtil.writeHtmlEncoded('"'),
				EncodeUtil.CHAR_ENTITY_QUOT);
		Assert.assertSame(EncodeUtil.writeHtmlEncoded('&'),
				EncodeUtil.CHAR_ENTITY_AMP);
		Assert
				.assertSame(EncodeUtil.writeHtmlEncoded('<'),
						EncodeUtil.CHAR_ENTITY_LT);
		Assert
				.assertSame(EncodeUtil.writeHtmlEncoded('>'),
						EncodeUtil.CHAR_ENTITY_GT);
		Assert.assertSame(EncodeUtil.writeHtmlEncoded('\''),
				EncodeUtil.CHAR_ENTITY_APOS_HTML);

		// test escapeXml(char) methods
		Assert.assertSame(EncodeUtil.xmlEncode('"'),
				EncodeUtil.CHAR_ENTITY_QUOT);
		Assert
				.assertSame(EncodeUtil.xmlEncode('&'),
						EncodeUtil.CHAR_ENTITY_AMP);
		Assert.assertSame(EncodeUtil.xmlEncode('<'), EncodeUtil.CHAR_ENTITY_LT);
		Assert.assertSame(EncodeUtil.xmlEncode('>'), EncodeUtil.CHAR_ENTITY_GT);
		Assert.assertSame(EncodeUtil.xmlEncode('\''),
				EncodeUtil.CHAR_ENTITY_APOS);

		// test with null
		testSqlEncodeWith(null, "NULL");

		// test with string containing no single-qoute
		testSqlEncodeWith("no single quote", "'no single quote'");

		// test with string containing single-quotes
		testSqlEncodeWith("lo'ts of s'ingle qu''otes",
				"'lo''ts of s''ingle qu''''otes'");
	}

	private void testSqlEncodeWith(String input, String result) {
		StringBuilder sb = new StringBuilder();
		sb.append("x");
		EncodeUtil.writeSqlEncoded(sb, input);
		sb.append("y");
		Assert.assertEquals(sb.toString(), "x" + result + 'y');

		Assert.assertEquals(EncodeUtil.sqlEncode(input), result);
	}

	@Test
	public void testUrlEncode() {
		Assert.assertEquals(EncodeUtil.urlEncode("abc 123&X+"),
				"abc+123%26X%2B");
		Assert.assertSame(EncodeUtil.urlEncode("abc123"),
		"abc123");
	}

	@Test
	public void testHtmlEncode() {
		// test with null
		testHtmlEncodeWith(null, "");

		// test with no escaped chars (escapeHtml(s) must return the same s)
		String s = "no escaped char";
		testHtmlEncodeWith(s, s);
		Assert.assertSame(EncodeUtil.htmlEncode(s), s);

		// test with strings containing chars to be escaped
		testHtmlEncodeWith("abc@ \" < > ' <> 123!",
				"abc@ &quot; &lt; &gt; &#29; &lt;&gt; 123!");
	}

	private void testHtmlEncodeWith(String input, String result) {
		StringBuilder sb = new StringBuilder();
		sb.append("x");
		EncodeUtil.writeHtmlEncoded(sb, input);
		sb.append("y");
		Assert.assertEquals(sb.toString(), "x" + result + 'y');

		StringWriter sw = new StringWriter();
		HtmlEncodeWriter hew = new HtmlEncodeWriter(sw);
		hew.append('x');
		hew.write(input);
		hew.write('y');
		Assert.assertEquals(sw.toString(), "x" + result + 'y');

		Assert.assertEquals(EncodeUtil.htmlEncode(input), result);

	}

	@Test
	public void testEscapeXml() {
		// test with null
		testXmlEncodeWith(null, "");

		// test with no escaped chars (escapeHtml(s) must return the same s)
		String s = "no escaped char";
		testXmlEncodeWith(s, s);
		Assert.assertSame(EncodeUtil.xmlEncode(s), s);

		// test with strings containing chars to be escaped
		testXmlEncodeWith("abc@ \" < > ' <> 123!",
				"abc@ &quot; &lt; &gt; &apos; &lt;&gt; 123!");
	}

	private void testXmlEncodeWith(String input, String result) {
		StringBuilder sb = new StringBuilder();
		sb.append("x");
		EncodeUtil.writeXmlEncoded(sb, input);
		sb.append("y");
		Assert.assertEquals(sb.toString(), "x" + result + 'y');

		Assert.assertEquals(EncodeUtil.xmlEncode(input), result);
	}

}
