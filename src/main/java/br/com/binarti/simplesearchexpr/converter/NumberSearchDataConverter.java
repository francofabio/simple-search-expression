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

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;

import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;


public class NumberSearchDataConverter extends AbstractSearchDataConverter {
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Object asSingleObject(SimpleSearchExpressionField field, String value, Map<String, Object> params, Class<?> targetType) {
		if (value == null) return null;
		if (targetType.isPrimitive()) {
			targetType = ClassUtils.primitiveToWrapper(targetType);
		}
		try {			
			Constructor constructor = targetType.getConstructor(String.class);
			return constructor.newInstance(value.trim());
		} catch (Exception e) {
			throw new SimpleSearchDataConverterException("Error converter " + value + " to " + targetType.getClass());
		}
	}

	@Override
	public boolean supports(Class<?> cls) {
		return Byte.class.isAssignableFrom(cls) || byte.class.isAssignableFrom(cls)
				|| Short.class.isAssignableFrom(cls) || short.class.isAssignableFrom(cls)
				|| Integer.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls)
				|| Long.class.isAssignableFrom(cls) || long.class.isAssignableFrom(cls)
				|| Float.class.isAssignableFrom(cls) || float.class.isAssignableFrom(cls)
				|| Double.class.isAssignableFrom(cls) || double.class.isAssignableFrom(cls)
				|| BigDecimal.class.isAssignableFrom(cls)
				|| BigInteger.class.isAssignableFrom(cls);
	}

}
