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
package br.com.binarti.simplesearchexpr.builders.jpql;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import br.com.binarti.simplesearchexpr.SimpleSearchExpression;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionException;
import br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperation;
import br.com.binarti.simplesearchexpr.builders.SimpleSearchExpressionBuilder;

public class SimpleSearchJPQLBuilder implements SimpleSearchExpressionBuilder<JPQLWhereClause> {

	private String getLike(String value) {
		if (value == null) {
			return "%";
		}
		return "%" + value + "%";
	}
	
	@Override
	public JPQLWhereClause build(SimpleSearchExpression searchExpression) {
		JPQLWhereClause jpqlCondition = new JPQLWhereClause();
		for (SimpleSearchRelationalOperation op : searchExpression.getOperations()) {
			final String name = op.getField().getName();
			final String field = op.getField().getFieldExpr();
			final Object value = op.getValue();
			switch (op.getOperator()) {
			case EQUALS:
				jpqlCondition.add(field + " = :" + name, name, value);
				break;
			case LIKE:
				jpqlCondition.add(field + " like :" + name, name, getLike(StringUtils.trim((String) value)));
				break;
			case INTERVAL:
				Iterator<Object> it = op.getValueAsCollection().iterator();
				Object value1 = it.next();
				Object value2;
				if (it.hasNext()) {
					value2 = it.next();
				} else {
					value2 = value1;
				}
				String paramBegin = name + "_begin";
				String paramEnd = name + "_end";
				jpqlCondition.add(field + " >= :" + paramBegin, paramBegin, value1);
				jpqlCondition.add(field + " <= :" + paramEnd, paramEnd, value2);
				break;
			case LIST:
				jpqlCondition.add(field + " in :" + name, name, op.getValueAsCollection());
				break;
			default:
				throw new SimpleSearchExpressionException("Operator " + op.getOperator() + " not supported");
			}
		}
		return jpqlCondition;
	}

}
