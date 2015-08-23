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

package com.mihaila.zutiltest.lang;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mihaila.zutil.lang.StringUtil;


public class StringUtilTest {

	@Test
	public void testCaseFuntions() {
		testCaseFuntionsHelper("justASimple_String", "just_a_simple_string",
				"JustASimpleString", "justASimpleString");

		testCaseFuntionsHelper("ABC_abc__1a", "a_b_c_abc__1a", "ABCAbc1a",
				"aBCAbc1a");

		testCaseFuntionsHelper("aBc", "a_bc", "ABc", "aBc");

		testCaseFuntionsHelper("DEf", "d_ef", "DEf", "dEf");
	}

	public void testCaseFuntionsHelper(String value,
			String expLowerUnderscoreCase, String expUpperCamelCase,
			String expLowerCamelCase) {
		String upperUnderscoreCase = StringUtil.toUpperUnderscoreCase(value);
		String lowerUnderscoreCase = StringUtil.toLowerUnderscoreCase(value);
		String upperCamelCase = StringUtil.toUpperCamelCase(value);
		String lowerCamelCase = StringUtil.toLowerCamelCase(value);
		Assert.assertEquals(upperUnderscoreCase, expLowerUnderscoreCase
				.toUpperCase());
		Assert.assertEquals(lowerUnderscoreCase, expLowerUnderscoreCase);
		Assert.assertEquals(upperCamelCase, expUpperCamelCase);
		Assert.assertEquals(lowerCamelCase, expLowerCamelCase);

	}
}
