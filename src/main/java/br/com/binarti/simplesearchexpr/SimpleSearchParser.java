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

import java.util.Date;
import java.util.regex.Pattern;

import br.com.binarti.simplesearchexpr.converter.SimpleSearchConverterResult;
import br.com.binarti.simplesearchexpr.converter.SimpleSearchDataConverter;
import br.com.binarti.simplesearchexpr.converter.SimpleSearchDataConverterException;
import br.com.binarti.simplesearchexpr.converter.SimpleSearchDataConverterFactory;

/**
 * Parser for simple search expression.<br/>
 * 
 * The simple search expression, is a simple string that define a conditional search, using fields and values.<br/>
 * 
 * Examples of simple search expression is:<br/>
 * createAt:01/03/2014<br/>
 * This expression will be represented by:<br/>
 * <pre>
 * [
 *    {
 *    	field: "createAt",
 *    	operator: EQUALS,
 *    	value: java.util.Date("01/03/2014")
 *    }
 * ]
 * </pre>
 * createAt:01/03/2014 job:Programmer
 * <pre>
 * [
 *    {
 *    	field: "createAt",
 *      operator: EQUALS,
 *      value: java.util.Date("01/03/2014")
 *    },
 *    {
 *    	field: "job",
 *    	operator: LIKE,
 *    	value: "Programmer"
 *    }
 * ]
 * </pre>
 * 
 * Default operator resolver by data type<br/>
 * <table border="1">
 * <tr>
 * 	<th>Type</th>
 *  <th>Operator</th>
 *  <th>Comments</th>
 * </tr>
 * <tr>
 * 	<td>java.lang.String</td>
 * 	<td>LIKE</td>
 *  <td>By default string not support other relational operations like: <strong>IN</strong> or <strong>INTERVAL</strong></td>
 * </tr>
 * <tr>
 * 	<td>java.lang.Number</td>
 * 	<td>EQUALS</td>
 * 	<td>When comma (,) is present in expression, the IN operator is used. When (-) is present in expression, the INTERVAL operator is used.</td>
 * </tr>
 * <tr>
 * 	<td>java.util.Date</td>
 * 	<td>EQUALS</td>
 * 	<td>When comma (,) is present in expression, the IN operator is used. When (-) is present in expression, the INTERVAL operator is used.</td>
 * </tr>
 * <tr>
 * 	<td>Others</td>
 * 	<td>EQUALS</td>
 * 	<td>When comma (,) is present in expression, the IN operator are used. Interval (-) operator is not supported.</td>
 * </tr> 
 * </table>
 * <br/>
 * Currently only the operators LIKE, EQUALS, INTERVAL or IN are supported.</br>
 * The definition of operator depends on the field type and/or expression value.</br>
 * To define a custom operator resolver, pass to plan constructor the custom operator resolver.
 * 
 * 
 * @author francofabio
 */
public class SimpleSearchParser {
	
	private static final String RE_WORD_SEPARATOR = "[\\W&&[^\\.]]";
	
	private SimpleSearchExpressionPlan plan;
	
	public SimpleSearchParser(SimpleSearchExpressionPlan plan) {
		this.plan = plan;
	}
	
	private SimpleSearchConverterResult convert(SimpleSearchExpressionField field, String value) {
		SimpleSearchDataConverter converter = field.getConverter();
		if (converter == null) {
			if (!SimpleSearchDataConverterFactory.existsConverterFor(field.getType())) {
				throw new SimpleSearchDataConverterException("No converter found for type " + field.getType().getName() + " and no converter found in the field. Field name: " + field.getName());
			}
			converter = SimpleSearchDataConverterFactory.converterFor(field.getType());
		}
		return converter.asObject(plan.getOperatorResolver(), field, value, field.getConverterParams(), field.getType());
	}
	
	private SimpleSearchRelationalOperation addOperation(SimpleSearchExpression expression, SimpleSearchExpressionField field, String value) {
		final SimpleSearchConverterResult converterResult = convert(field, value);
		final SimpleSearchRelationalOperation operation = new SimpleSearchRelationalOperation(field, converterResult.getOperator(), converterResult.getValue());
		expression.addOperation(operation);
		return operation;
	}
	
	public SimpleSearchExpression parse(String searchExpression) {
		final SimpleSearchExpression expression = new SimpleSearchExpression();
		final Pattern separatorPattern = Pattern.compile(RE_WORD_SEPARATOR);
		
		String fieldName = null;
		StringBuilder fieldValue = new StringBuilder();
		StringBuilder lastWord = new StringBuilder();
		
		final char[] expr = searchExpression.toCharArray();
		for (int i=0; i < expr.length; i++) {
			char c = expr[i];
			if (c == ':') {
				if (fieldName != null && fieldValue.length() > 0) {
					SimpleSearchExpressionField field = plan.getField(fieldName);
					if (field != null) {
						addOperation(expression, field, fieldValue.toString().trim());
					}
					fieldName = null;
				}
				
				if (lastWord.length() > 0) {
					fieldName = lastWord.toString();
					lastWord.delete(0, lastWord.length());
				}
				fieldValue.delete(0, fieldValue.length());
			} else {
				if (separatorPattern.matcher(String.valueOf(c)).matches()) {
					lastWord.append(c);
					fieldValue.append(lastWord);
					lastWord.delete(0, lastWord.length());
				} else {
					lastWord.append(c);
				}
			}
		}
		if (lastWord.length() > 0) {
			fieldValue.append(lastWord);
		}
		if (fieldName != null && fieldValue.length() > 0) {
			String value = fieldValue.toString().trim();
			SimpleSearchExpressionField field = plan.getField(fieldName);
			if (field != null) {
				addOperation(expression, field, value);
			}
		} else if (fieldName == null && fieldValue.length() > 0 && expression.getOperations().isEmpty()) {
			String value = fieldValue.toString().trim();
			SimpleSearchExpressionField field = plan.getAppropriateFieldByAnonymousExpression(value);
			if (field != null) {
				addOperation(expression, field, value);
			}
		}
		
		return expression;
	}

	public static void main(String[] args) {
		final SimpleSearchExpressionPlan plan = new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("nome", String.class), 
				new SimpleSearchExpressionField("bairro", String.class), new SimpleSearchExpressionField("data", Date.class),
				new SimpleSearchExpressionField("cidade", String.class));
		final SimpleSearchExpression e = new SimpleSearchParser(plan).parse("nome:fabio bairro:porto de santana data:01/01/2014-20/01/2014 cidade:cariacica nome:a");
		System.out.println(e.getOperations().size());
		for (SimpleSearchRelationalOperation op : e.getOperations()) {
			System.out.println(op.getField().getName() + " " + op.getOperator() + " " + op.getValue());
		}
	}
	
}
