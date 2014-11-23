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
import java.util.Map;

import br.com.binarti.simplesearchexpr.OperatorResolver;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;
import br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator;

public abstract class AbstractSearchDataConverter implements SimpleSearchDataConverter {
	
	@Override
	public SimpleSearchConverterResult asObject(OperatorResolver operatorResolver, SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType)
			throws SimpleSearchDataConverterException {
		SimpleSearchRelationalOperator operator = operatorResolver.resolveOperator(targetType, value, field);
		if (value == null) return new SimpleSearchConverterResult(operator, null);
		
		Object objectValue;
		switch (operator) {
		case LIKE:
		case EQUALS:
			objectValue = asSingleObject(field, value, params, targetType);
			break;
		case LIST:
			objectValue = processListValue(field, value, params, targetType);
			break;
		case INTERVAL:
			objectValue = processIntervalValue(field, value, params, targetType);
			break;
		default:
			throw new SimpleSearchDataConverterException("Operator " + operator + " not supported");
		}
		return new SimpleSearchConverterResult(operator, objectValue);
	}
	
	private Object processIntervalValue(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) {
		final String[] striped = value.split(OperatorResolver.INTERVAL_SEPARATOR);
		final List<Object> interval = new ArrayList<Object>();
		if (striped.length == 0) {
			throw new SimpleSearchDataConverterException("No interval expression found " + value);
		}
		if (striped.length == 1) { //When only one value is found, repeat first value
			Object converted = asSingleObject(field, striped[0], params, targetType);
			interval.add(converted);
			interval.add(converted);
		} else if (striped.length >= 2) {
			interval.add(asSingleObject(field, striped[0], params, targetType));
			interval.add(asSingleObject(field, striped[1], params, targetType));
		}
		return interval;
	}

	private Object processListValue(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) {
		final String[] striped = value.split(OperatorResolver.LIST_SEPARATOR);
		final List<Object> list = new ArrayList<Object>();
		if (striped.length == 0) {
			throw new SimpleSearchDataConverterException("No list expression found " + value);
		}
		for (String singleValue : striped) {
			list.add(asSingleObject(field, singleValue, params, targetType));
		}
		return list;
	}

	protected abstract Object asSingleObject(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType);
	
}
