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

import java.util.Map;

import br.com.binarti.simplesearchexpr.OperatorResolver;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;


public class StringSearchDataConverter implements SimpleSearchDataConverter {

	@Override
	public SimpleSearchConverterResult asObject(OperatorResolver operatorResolver, SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) throws SimpleSearchDataConverterException {
		return new SimpleSearchConverterResult(operatorResolver.resolveOperator(targetType, value, field), value);
	}
	
	@Override
	public boolean supports(Class<?> cls) {
		return String.class.isAssignableFrom(cls);
	}

	
}
