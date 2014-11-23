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

import java.util.ArrayList;
import java.util.Collection;

public class SimpleSearchRelationalOperation {
	private SimpleSearchExpressionField field;
	private SimpleSearchRelationalOperator operator;
	/*
	 * The value of operation, should be a single object or a list of object.
	 * When operator is a collection. The value is a collection instance, otherwise is a single Object
	 */
	private Object value;

	public SimpleSearchRelationalOperation(SimpleSearchExpressionField field, SimpleSearchRelationalOperator operator) {
		this.field = field;
		this.operator = operator;
		if (operator.isCollection()) {
			value = new ArrayList<Object>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public SimpleSearchRelationalOperation(SimpleSearchExpressionField field, SimpleSearchRelationalOperator operator, Object value) {
		this(field, operator);
		if (operator.isCollection()) {
			if (value instanceof Collection) {
				((Collection<Object>) this.value).addAll((Collection<Object>) value);
			} else {
				((Collection<Object>) this.value).add(value);
			}
		} else {
			this.value = value;
		}
	}

	public SimpleSearchExpressionField getField() {
		return field;
	}

	public SimpleSearchRelationalOperator getOperator() {
		return operator;
	}

	/**
	 * Value of the operation.
	 * 
	 * @return Collection when operator is collection, otherwise return object value.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * @return Value of the operation when operator is collection.
	 * @throws RuntimeException If the operator is not collection
	 */
	@SuppressWarnings("unchecked")
	public Collection<Object> getValueAsCollection() {
		if (!this.operator.isCollection()) {
			throw new RuntimeException("This operation is not configured to collection");
		}
		return ((Collection<Object>) this.value);
	}

	public void setValue(Object value) {
		if (this.operator.isCollection() && value != null && !(value instanceof Collection)) {
			throw new RuntimeException("This operation is configured as collection, therefore only allowed Collection");
		}
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public void addValue(Object value) {
		if (!operator.isCollection()) {
			throw new RuntimeException("This operation is not configured to collection");
		}
		((Collection<Object>) this.value).add(value);
	}

	public void addValues(Collection<Object> values) {
		for (Object value : values) {
			addValue(value);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		SimpleSearchRelationalOperation other = (SimpleSearchRelationalOperation) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		return true;
	}

}
