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

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;


public class DateSearchDataConverter extends AbstractSearchDataConverter {

	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	private static final String PATTERN_PARAM_NAME = "pattern";
	
	private String defaultPattern;
	
	public DateSearchDataConverter() {
		this.defaultPattern = DEFAULT_DATE_FORMAT;
	}
	
	public DateSearchDataConverter(String defaultPattern) {
		this.defaultPattern = defaultPattern;
	}
	
	@Override
	public boolean supports(Class<?> cls) {
		return Date.class.isAssignableFrom(cls);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object asSingleObject(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) {
		String[] parsePatterns = new String[] { defaultPattern };
		if (params != null && params.containsKey(PATTERN_PARAM_NAME)) {
			Object paramValue = params.get(PATTERN_PARAM_NAME);
			if (paramValue instanceof Collection) {
				parsePatterns = ((Collection<String>) paramValue).toArray(new String[0]);
			} else if (paramValue instanceof String[]) {
				parsePatterns = (String[]) paramValue;
			} else if (paramValue instanceof String) {
				parsePatterns = new String[] { (String) params.get(PATTERN_PARAM_NAME) };
			} else {
				throw new SimpleSearchDataConverterException("Invalid param pattern, expected Collection<String>, String[] or String, but found " + paramValue.getClass());
			}
		}
		try {
			return DateUtils.parseDate(value.trim(), parsePatterns);
		} catch (ParseException e) {
			throw new SimpleSearchDataConverterException("Date " + value + " invalid format");
		}
	}

	

}
