/***
 * Copyright (c) 2014 Binar tecnologia da informação - www.binarti.com.br All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.binarti.simplesearchexpr.converter;

import java.util.ArrayList;
import java.util.List;

public final class SimpleSearchDataConverterFactory {

	private static final List<SimpleSearchDataConverter> converters;
	
	static {
		converters = new ArrayList<SimpleSearchDataConverter>();
		//Default converters
		registerConverter(new StringSearchDataConverter());
		registerConverter(new NumberSearchDataConverter());
		registerConverter(new DateSearchDataConverter());
		registerConverter(new EnumSearchDataConverter());
	}
	
	public static void registerConverter(SimpleSearchDataConverter converter) {
		//Add converter on begin of list, for use always the last registered
		converters.add(0, converter);
	}
	
	public static void unregisterConverter(SimpleSearchDataConverter converter) {
		int index = converters.indexOf(converter);
		if (index > -1) {
			converters.remove(index);
		}
	}
	
	private static SimpleSearchDataConverter findConverter(Class<?> cls) {
		for (SimpleSearchDataConverter converter : converters) {
			if (converter.supports(cls)) {
				return converter;
			}
		}
		return null;
	}
	
	public static boolean existsConverterFor(Class<?> cls) {
		return findConverter(cls) != null;
	}
	
	public static SimpleSearchDataConverter converterFor(Class<?> cls) {
		SimpleSearchDataConverter converter = findConverter(cls);
		if (converter == null) {
			throw new SimpleSearchDataConverterException("No converter found for class " + cls.getName());
		}
		return converter;
	}
	
}
