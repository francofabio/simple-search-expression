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
package br.com.binarti.simplesearchexpr.builders.mongodb;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import br.com.binarti.simplesearchexpr.SimpleSearchExpression;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionException;
import br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperation;
import br.com.binarti.simplesearchexpr.builders.SimpleSearchExpressionBuilder;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * The simple search builder for mongo db
 * 
 * @author francofabio
 *
 */
public class SimpleSearchMongoDBBuilder implements SimpleSearchExpressionBuilder<DBObject> {

	private BasicDBList mongoList(Collection<Object> collection) {
		BasicDBList result = new BasicDBList();
		for (Object o : collection) {
			result.add(o);
		}
		return result;
	}
	
	@Override
	public DBObject build(SimpleSearchExpression searchExpression) {
		BasicDBObject root = new BasicDBObject();
		if (searchExpression.getOperations().isEmpty()) {
			return root;
		}
		for (SimpleSearchRelationalOperation op : searchExpression.getOperations()) {
			final String field = op.getField().getFieldExpr();
			final Object value = op.getValue();
			switch (op.getOperator()) {
			case EQUALS:
				root.put(field, value);
				break;
			case LIKE:
				root.put(field, new BasicDBObject("$regex", StringUtils.trim((String) value)).append("$options", "i"));
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
				root.append(field, new BasicDBObject("$gte", value1).append("$lte", value2));
				break;
			case LIST:
				root.append(field, new BasicDBObject("$in", mongoList(op.getValueAsCollection())));
				break;
			default:
				throw new SimpleSearchExpressionException("Operator " + op.getOperator() + " not supported");
			}
		}
		return root;
	}

}
