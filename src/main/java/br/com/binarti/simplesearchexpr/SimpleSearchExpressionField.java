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
package br.com.binarti.simplesearchexpr;

import java.util.Map;

import br.com.binarti.simplesearchexpr.converter.SimpleSearchDataConverter;

public class SimpleSearchExpressionField {

	private String name;
	// Field name or expression to use in search condition build
	private String fieldExpr;
	private Class<?> type;
	private SimpleSearchDataConverter converter;
	private Map<String, Object> converterParams;
	private SimpleSearchRelationalOperator defaultOperator;

	public SimpleSearchExpressionField(String name, Class<?> type) {
		this.name = name;
		this.fieldExpr = name;
		this.type = type;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type);
		this.defaultOperator = defaultOperator;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, SimpleSearchDataConverter converter) {
		this(name, type);
		this.converter = converter;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, SimpleSearchDataConverter converter, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type);
		this.converter = converter;
		this.defaultOperator = defaultOperator;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, Map<String, Object> converterParams) {
		this(name, type);
		this.converterParams = converterParams;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, Map<String, Object> converterParams, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type);
		this.converterParams = converterParams;
		this.defaultOperator = defaultOperator;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, SimpleSearchDataConverter converter, Map<String, Object> converterParams) {
		this(name, type);
		this.converter = converter;
		this.converterParams = converterParams;
	}
	
	public SimpleSearchExpressionField(String name, Class<?> type, SimpleSearchDataConverter converter, Map<String, Object> converterParams, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type);
		this.converter = converter;
		this.converterParams = converterParams;
		this.defaultOperator = defaultOperator;
	}

	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type) {
		this(name, type);
		this.fieldExpr = fieldExpr;
	}
	
	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type);
		this.fieldExpr = fieldExpr;
		this.defaultOperator = defaultOperator;
	}
	
	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type, SimpleSearchDataConverter converter) {
		this(name, type, converter);
		this.fieldExpr = fieldExpr;
	}
	
	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type, SimpleSearchDataConverter converter, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type, converter);
		this.fieldExpr = fieldExpr;
		this.defaultOperator = defaultOperator;
	}
	
	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type, SimpleSearchDataConverter converter, Map<String, Object> converterParams) {
		this(name, type, converter);
		this.fieldExpr = fieldExpr;
		this.converterParams = converterParams;
	}
	
	public SimpleSearchExpressionField(String name, String fieldExpr, Class<?> type, SimpleSearchDataConverter converter, Map<String, Object> converterParams, SimpleSearchRelationalOperator defaultOperator) {
		this(name, type, converter);
		this.fieldExpr = fieldExpr;
		this.converterParams = converterParams;
		this.defaultOperator = defaultOperator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldExpr() {
		return fieldExpr;
	}

	public void setFieldExpr(String fieldExpr) {
		this.fieldExpr = fieldExpr;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public SimpleSearchDataConverter getConverter() {
		return converter;
	}

	public void setConverter(SimpleSearchDataConverter converter) {
		this.converter = converter;
	}
	
	public Map<String, Object> getConverterParams() {
		return converterParams;
	}

	public void setConverterParams(Map<String, Object> converterParams) {
		this.converterParams = converterParams;
	}
	
	public SimpleSearchRelationalOperator getDefaultOperator() {
		return defaultOperator;
	}

	public void setDefaultOperator(SimpleSearchRelationalOperator operator) {
		this.defaultOperator = operator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleSearchExpressionField other = (SimpleSearchExpressionField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
