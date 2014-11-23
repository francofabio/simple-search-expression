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

import static br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator.EQUALS;
import static br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator.INTERVAL;
import static br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator.LIKE;
import static br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator.LIST;

import java.util.Date;

import org.apache.commons.lang3.ClassUtils;

public class DefaultOperatorResolver implements OperatorResolver { 
	
	protected boolean isIntervalOperator(String value, Class<?> type) {
		if (value != null && (Number.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type))) {
			if (value.contains(INTERVAL_SEPARATOR)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isListOperator(String value, Class<?> fieldType) {
		if (value != null && value.contains(LIST_SEPARATOR)) {
			return true;
		}
		return false;
	}
	
	@Override
	public SimpleSearchRelationalOperator resolveOperator(Class<?> fieldType, String value, SimpleSearchExpressionField field) {
		if (String.class.isAssignableFrom(fieldType)) {
			return (field.getDefaultOperator() == null) ? LIKE : field.getDefaultOperator();
		}
		if (fieldType.isPrimitive()) {
			fieldType = ClassUtils.primitiveToWrapper(fieldType);
		}
		SimpleSearchRelationalOperator operator = (field.getDefaultOperator() == null) ? EQUALS : field.getDefaultOperator();
		if (value != null) {
			if (Number.class.isAssignableFrom(fieldType) || Date.class.isAssignableFrom(fieldType)) {
				if (value.contains(INTERVAL_SEPARATOR)) {
					operator = INTERVAL;
				}
			}
			if (value.contains(LIST_SEPARATOR)) {
				operator = LIST;
			}
		}
		return operator;
	}

}
