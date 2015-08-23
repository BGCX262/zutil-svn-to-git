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

package com.mihaila.zutil.factory;

import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Registry of factory classes (implementers of <code>IFactory</code>. This
 * class keeps references to <code>IFactory<T</code> objects, associated with
 * a key, tipically the name of the type T. Also the
 * <code>RegistryManager</code> can create runtime factory objects for classes
 * with default constructor (via runtime bytecode manipulation. The methods of
 * this class are thread-safe.
 */
public class FactoryManager {

	/**
	 * Parameters for the m_factories Map. INITIAL_CAPACITY and LOAD_FACTOR are
	 * the same as the defaults for a <code>SynchronizedHashMap</code>, but
	 * CONCURRENT_MAP_CONCURRENCY_LEVEL is reduced from 16 updating threads to 1
	 * because the updates are rare.
	 */
	private static final int CONCURRENT_MAP_INITIAL_CAPACITY = 16;

	private static final float CONCURRENT_MAP_LOAD_FACTOR = 0.75f;

	private static final int CONCURRENT_MAP_CONCURRENCY_LEVEL = 1;

	private static ConcurrentHashMap<String, IFactory<?>> m_factories = new ConcurrentHashMap<String, IFactory<?>>(
			CONCURRENT_MAP_INITIAL_CAPACITY, CONCURRENT_MAP_LOAD_FACTOR,
			CONCURRENT_MAP_CONCURRENCY_LEVEL);

	/**
	 * Used o generate <code>IFactory</code> objects at runtime.
	 */
	private static ClassPool m_classPool = ClassPool.getDefault();

	/**
	 * The number of generated runtime factories.
	 */
	private static int m_numRuntimeFactories = 0;

	/**
	 * If true, the registry tries to create a runtime factory object when
	 * needed (if it was not previously set).
	 */
	private static boolean m_autoGenerateFactories = true;

	static {
		// add the ClassLoader that loads FactoryManager into the class pool
		m_classPool.insertClassPath(new ClassClassPath(FactoryManager.class));
	}

	/**
	 * Returns the state of the autoGenerateFactories field. If true, the
	 * registry tries to create a runtime factory object when needed (if it was
	 * not previously set).
	 * 
	 * @return the state of the autoGenerateFactories field
	 */
	public static synchronized boolean getAutoGenerateFactories() {
		return m_autoGenerateFactories;
	}

	/**
	 * Set the state of the autoGenerateFactories field. If true, the registry
	 * tries to create a runtime factory object when needed (if it was not
	 * previously set).
	 * 
	 * @param autoGenerateFactories
	 */
	public static synchronized void setAutoGenerateFactories(
			boolean autoGenerateFactories) {
		m_autoGenerateFactories = autoGenerateFactories;
	}

	/**
	 * Returns a new instance object of the specified type. If
	 * autoGenerateFactories is true and no factory was registered for that
	 * type, the registry tries to create one.
	 * 
	 * @param klass
	 * @return a new instance of the specified type
	 */
	// because of (T) cast
	public static <T> T newInstance(Class<T> klass) {
		IFactory<T> factory = (m_autoGenerateFactories) ? getFactoryAuto(klass)
				: getFactory(klass);
		return (factory == null) ? null : factory.newInstance();
	}

	/**
	 * Returns a new instance object created by the factory associated with the
	 * specified key. If autoGenerateFactories is true and no factory was
	 * registered for that key, the registry tries to create one. If key is not
	 * a type name and no factory was registered, null is returned.
	 * 
	 * @param key
	 * @return a new instance object created by the factory associated with the
	 *         specified key
	 */
	public static Object newInstance(String key) {
		IFactory<?> factory = (m_autoGenerateFactories) ? getFactoryAuto(key)
				: getFactory(key);
		return (factory == null) ? null : factory.newInstance();
	}

	/**
	 * Returns the factory associated with the the specified type.
	 * 
	 * @param klass
	 * @return the factory associated with the the specified type
	 */
	// because of (IFactory<T>) cast
	public static <T> IFactory<T> getFactory(Class<T> klass) {
		@SuppressWarnings("unchecked")
		IFactory<T> factory = (IFactory<T>) m_factories.get(klass.getName());
		return factory;
	}

	/**
	 * Returns the factory associated with the the specified key.
	 * 
	 * @param key
	 * @return the factory associated with the the specified key
	 */
	public static IFactory<?> getFactory(String key) {
		return m_factories.get(key);
	}

	/**
	 * Returns the factory associated with the the specified type. If no factory
	 * is registered for the specified type, than a new factory is generated,
	 * stored in the registry and returned.
	 * 
	 * @param klass
	 * @return the factory associated with the the specified type
	 */
	// because of (IFactory<T>) cast
	public static <T> IFactory<T> getFactoryAuto(Class<T> klass) {
		String key = klass.getName();
		@SuppressWarnings("unchecked")
		IFactory<T> factory = (IFactory<T>) m_factories.get(key);
		if ((factory == null) && (m_autoGenerateFactories)) {
			return getFactoryAutoSynchro(klass);
		}
		return factory;
	}

	/**
	 * Returns the factory associated with the the specified key. If no factory
	 * is registered for the specified key, the registry tries to create one. If
	 * key is not a type name and no factory was registered, null is returned.
	 * 
	 * @param key
	 * @return the factory associated with the the specified key
	 */
	public static IFactory<?> getFactoryAuto(String key) {
		IFactory<?> factory = m_factories.get(key);
		if ((factory == null) && (m_autoGenerateFactories)) {
			if (key.length() == 0) {
				return null;
			}
			char ch = key.charAt(0);
			// if key could be a class name
			if ((ch >= 'a') && (ch < 'z') || (ch >= 'A') && (ch < 'Z')) {
				Class<?> klass;
				try {
					klass = Class.forName(key, true, Thread.currentThread()
							.getContextClassLoader());
				} catch (ClassNotFoundException e) {
					return null;
				}
				return getFactoryAutoSynchro(klass);
			} else {
				return null;
			}
		}
		return factory;
	}

	/**
	 * Register the specified factory. Previously associated factory on the
	 * specified type is overwritten (a null factory value can be written to
	 * remove the existing factory).
	 * 
	 * @param klass
	 * @param factory
	 * @return the previous registered factory, if any
	 */
	public static <T> IFactory<T> setFactory(Class<T> klass, IFactory<T> factory) {
		return setFactory(klass.getName(), factory);
	}

	/**
	 * Register the specified factory. Previously associated factory with the
	 * specifed key is overwritten (a null factory value can be written to
	 * remove the existing factory).
	 * 
	 * @param key
	 * @param factory
	 * @return the previous registered factory, if any
	 * 
	 */
	public static <T> IFactory<T> setFactory(String key, IFactory<T> factory) {
		@SuppressWarnings("unchecked")
		IFactory<T> previousFactory = (IFactory<T>) m_factories.put(key,
				factory);
		return previousFactory;
	}

	/**
	 * Generates a factory object for the specified type.
	 * 
	 * @param type
	 * @return a factory object for the specified type
	 */
	// because of (IFactory<T>) cast
	@SuppressWarnings("unchecked")
	public static synchronized <T> IFactory<T> generateRuntimeFactory(
			Class<T> type) {
		String typeName = type.getName();
		IFactory runtimeFactory = null;
		m_numRuntimeFactories++;
		String factoryTypeName = typeName + "__RuntimeFactory"
				+ m_numRuntimeFactories;
		try {
			CtClass ctClass = m_classPool.makeClass(factoryTypeName);
			ctClass.addInterface(m_classPool.get(IFactory.class.getName()));
			CtMethod ctMethod = new CtMethod(m_classPool.get(Object.class
					.getName()), "newInstance", null, ctClass);
			ctMethod.setBody("return new " + typeName + "();");
			ctClass.addMethod(ctMethod);
			Class factoryType = ctClass.toClass();
			ctClass.detach();
			runtimeFactory = (IFactory) factoryType.newInstance();
			if (runtimeFactory == null) {
				throw new NullPointerException("Null runtime factory.");
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error creating runtime factory for class: " + typeName
							+ '.', e);
		}
		return runtimeFactory;
	}

	/**
	 * Clear all the registered factories.
	 */
	public static void clear() {
		m_factories.clear();
	}

	private static synchronized <T> IFactory<T> getFactoryAutoSynchro(
			Class<T> klass) {
		String key = klass.getName();
		@SuppressWarnings("unchecked")
		IFactory<T> factory = (IFactory<T>) m_factories.get(key);
		if (factory == null) {
			factory = generateRuntimeFactory(klass);
			m_factories.put(key, factory);
		}
		return factory;
	}

}
