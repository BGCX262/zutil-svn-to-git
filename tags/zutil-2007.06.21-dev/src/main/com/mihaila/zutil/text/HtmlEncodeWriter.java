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
import java.io.PrintWriter;
import java.io.Writer;

import com.mihaila.zutil.lang.WrappedInRuntimeException;


/**
 * This <code>PrintWriter</code> subclass transforms the input to be suitable
 * to be used in a HTML document. The characters: " & < > are escaped with their
 * correspondent character entities. "'" is replaced by &#29;.
 * 
 */
public class HtmlEncodeWriter extends PrintWriter {

	public HtmlEncodeWriter() {
		this(null);
	}

	/**
	 * @param out
	 *            the <code>Writer</code> object used as output
	 */
	public HtmlEncodeWriter(Writer out) {
		super(out);
	}

	/**
	 * Returns the <code>Writer</code> object used as output.
	 * 
	 * @return the <code>Writer</code> object used as output
	 */
	public Writer getOut() {
		return out;
	}

	/**
	 * Sets the <code>Writer</code> object used as output.
	 * 
	 * @param out
	 */
	public void setOut(Writer out) {
		this.out = out;
	}

	@Override
	public void write(char[] buf, int off, int len) {
		String escaped = EncodeUtil
				.htmlEncode(new String(buf, off, len));
		writeToOut(escaped);
	}

	@Override
	public void write(char[] buf) {
		String escaped = EncodeUtil.htmlEncode(new String(buf));
		writeToOut(escaped);
	}

	@Override
	public void write(int c) {
		char ch = (char) c;
		try {
			if (EncodeUtil.mustEscapeCharInHtml(ch)) {
				out.write(EncodeUtil.writeHtmlEncoded(ch));
			} else {
				out.write(c);
			}
		} catch (IOException e) {
			throw new WrappedInRuntimeException(e);
		}
	}

	@Override
	public void write(String s, int off, int len) {
		EncodeUtil.writeHtmlEncoded(out, s.substring(off, off + len));
	}

	@Override
	public void write(String s) {
		EncodeUtil.writeHtmlEncoded(out, s);
	}

	/**
	 * Write to the output <code>Writer</code> object.S
	 * 
	 * @param s
	 */

	private void writeToOut(String s) {
		try {
			out.write(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
