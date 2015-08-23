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

package com.mihaila.zutiltest.factory;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mihaila.zutil.factory.FactoryManager;
import com.mihaila.zutil.factory.IFactory;


public class FactoryRegistryTest {

	static class A {

	}

	static class B {
		public boolean flag;
	}

	static class FactoryB implements IFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see zutil.factory.IFactory#newInstance()
		 */
		public B newInstance() {
			B b = new B();
			b.flag = true;
			return b;
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		FactoryManager.clear();
		FactoryManager.setAutoGenerateFactories(true);
	}

	@Test
	public void testAutoGenerateFactories() {
		A a = FactoryManager.newInstance(A.class);
		Assert.assertNotNull(a);

		FactoryManager.setAutoGenerateFactories(false);
		IFactory<B> factoryB = FactoryManager.getFactory(B.class);
		Assert.assertNull(factoryB);
		B b = FactoryManager.newInstance(B.class);
		Assert.assertNull(b);

		FactoryManager.setAutoGenerateFactories(true);
		factoryB = FactoryManager.getFactory(B.class);
		Assert.assertNotNull(factoryB);
		b = FactoryManager.newInstance(B.class);
		Assert.assertNotNull(b);

		IFactory<B> factoryB2 = FactoryManager.getFactory(B.class);
		Assert.assertEquals(factoryB, factoryB2);
	}

	@Test
	public void testExistingFactory() {
		FactoryB factoryB = new FactoryB();
		FactoryManager.setFactory(B.class, factoryB);
		Assert.assertEquals(FactoryManager.getFactory(B.class), factoryB);
		B b = FactoryManager.newInstance(B.class);
		Assert.assertSame(b.flag, true);
	}

	@Test
	public void testNonClassNameKey() {
		B b = (B) FactoryManager.newInstance("test");
		Assert.assertNull(b);
		FactoryB factoryB = new FactoryB();
		FactoryManager.setFactory("test", factoryB);
		b = (B) FactoryManager.newInstance("test");
		Assert.assertNotNull(b);
	}

	@Test
	public void testGenerateRuntimeFactory() {
		IFactory<A> factoryA = FactoryManager.generateRuntimeFactory(A.class);
		Assert.assertNotNull(factoryA);
		IFactory<A> factoryA2 = FactoryManager.generateRuntimeFactory(A.class);
		Assert.assertNotNull(factoryA2);
		Assert.assertNotSame(factoryA, factoryA2);
	}
}
