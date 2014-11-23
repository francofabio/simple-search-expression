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

import org.apache.commons.lang3.StringUtils;

import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;

/**
 * This converter is not part of the standard converters. To use it, explicit in the search field mapping.<br/>
 * <strong>Parameters</strong>
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Type</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>Size</td>
 * <td>Integer</td>
 * <td>Size of numeric string. If the size of the string pass is less of size, the result string will be filled to complete the size</td>
 * </tr>
 * </table>
 * @author francofabio
 *
 */
public class StringNumericSearchDataConverter extends AbstractSearchDataConverter {

	private static final String PARAM_SIZE = "size";
	private static final String FILL_CHAR = "0";
	
	@Override
	public boolean supports(Class<?> cls) {
		return false;
	}

	@Override
	protected Object asSingleObject(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) {
		if (value == null) return null;
		value = value.replaceAll("\\D", "");
		if (params != null && params.containsKey(PARAM_SIZE)) {
			int size = (Integer) params.get(PARAM_SIZE);
			return StringUtils.leftPad(value, size, FILL_CHAR);
		}
		return value;
	}

}
