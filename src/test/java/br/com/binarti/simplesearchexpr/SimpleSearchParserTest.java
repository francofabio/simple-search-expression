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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import br.com.binarti.simplesearchexpr.DefaultOperatorResolver;
import br.com.binarti.simplesearchexpr.OperatorResolver;
import br.com.binarti.simplesearchexpr.SimpleSearchExpression;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionField;
import br.com.binarti.simplesearchexpr.SimpleSearchExpressionPlan;
import br.com.binarti.simplesearchexpr.SimpleSearchParser;
import br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperation;
import br.com.binarti.simplesearchexpr.SimpleSearchRelationalOperator;
import br.com.binarti.simplesearchexpr.converter.StringNumericSearchDataConverter;
import br.com.binarti.simplesearchexpr.test.OperationType;

public class SimpleSearchParserTest {

	@Test
	public void shouldParseSimpleSearchExpressionWithOneField() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name",
				String.class)));
		SimpleSearchExpression expr = parser.parse("name:jason ");

		assertEquals(1, expr.getOperations().size());

		final SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals("name", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("jason", relationalOperation.getValue());
	}

	@Test
	public void shouldParseSimpleSearchExpressionWithTwoFields() throws ParseException {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id", Integer.class),
				new SimpleSearchExpressionField("date", Date.class)));
		SimpleSearchExpression expr = parser.parse("id:1 date:10/06/2014");

		assertEquals(2, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals("id", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(1, relationalOperation.getValue());

		relationalOperation = expr.getOperations().get(1);
		assertEquals("date", relationalOperation.getField().getName());
		assertEquals("date", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(DateUtils.parseDate("10/06/2014", "dd/MM/yyyy"), relationalOperation.getValue());
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithGroupOfFields() throws ParseException {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id", Integer.class),
				new SimpleSearchExpressionField("date", Date.class),
				new SimpleSearchExpressionField("value", Double.class),
				new SimpleSearchExpressionField("operationType", String.class)));
		SimpleSearchExpression expr = parser.parse("id:1 date:10/06/2014-13/06/2014 value:100.0 operationType:Input");

		assertEquals(4, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(1, relationalOperation.getValue());

		relationalOperation = expr.getOperations().get(1);
		assertEquals("date", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.INTERVAL, relationalOperation.getOperator());
		assertEquals(2, relationalOperation.getValueAsCollection().size());
		assertArrayEquals(asList(DateUtils.parseDate("10/06/2014", "dd/MM/yyyy"),
				DateUtils.parseDate("13/06/2014", "dd/MM/yyyy")).toArray(new Date[0]),
				relationalOperation.getValueAsCollection().toArray(new Date[0]));
		
		relationalOperation = expr.getOperations().get(2);
		assertEquals("value", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(new Double(100.0), relationalOperation.getValue());
		
		relationalOperation = expr.getOperations().get(3);
		assertEquals("operationType", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("Input", relationalOperation.getValue());
	}

	@Test
	public void shouldParseSimpleSearchExpressionWithInterval() throws ParseException {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("date", Date.class)));
		SimpleSearchExpression expr = parser.parse("date:01/06/2014-30/06/2014");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("date", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.INTERVAL, relationalOperation.getOperator());
		assertEquals(2, relationalOperation.getValueAsCollection().size());
		assertArrayEquals(asList(DateUtils.parseDate("01/06/2014", "dd/MM/yyyy"),
				DateUtils.parseDate("30/06/2014", "dd/MM/yyyy")).toArray(new Date[0]),
				relationalOperation.getValueAsCollection().toArray(new Date[0]));
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithInOperation() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id",
				Integer.class)));
		SimpleSearchExpression expr = parser.parse("id:1,2,3,4,5");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIST, relationalOperation.getOperator());
		assertEquals(5, relationalOperation.getValueAsCollection().size());
		assertArrayEquals(asList(1,2,3,4,5).toArray(new Integer[0]), relationalOperation.getValueAsCollection().toArray(new Integer[0]));
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithEnumField() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("operationType", OperationType.class)));
		SimpleSearchExpression expr = parser.parse("operationType:Input");
		
		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("operationType", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(OperationType.Input, relationalOperation.getValue());
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithEnumFieldInOperation() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("operationType", OperationType.class)));
		SimpleSearchExpression expr = parser.parse("operationType:Input, Output");
		
		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("operationType", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIST, relationalOperation.getOperator());
		assertArrayEquals(asList(OperationType.Input,OperationType.Output).toArray(new OperationType[0]), 
				relationalOperation.getValueAsCollection().toArray(new OperationType[0]));
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionDateWithCustomPatterns() throws Exception {
		final Map<String, Object> dateFieldParams = new HashMap<String, Object>() {
			{
				put("pattern", Arrays.asList("dd/MM/yyyy", "yyyyMMdd"));
			}
		};
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("date", Date.class, dateFieldParams)));
		SimpleSearchExpression expr = parser.parse("date:01/06/2014-20140630");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("date", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.INTERVAL, relationalOperation.getOperator());
		assertEquals(2, relationalOperation.getValueAsCollection().size());
		assertArrayEquals(asList(DateUtils.parseDate("01/06/2014", "dd/MM/yyyy"),
				DateUtils.parseDate("30/06/2014", "dd/MM/yyyy")).toArray(new Date[0]),
				relationalOperation.getValueAsCollection().toArray(new Date[0]));
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionCustomConverter() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>() {
			{
				put("size", 6);
			}
		};
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id", String.class, new StringNumericSearchDataConverter(), params)));
		SimpleSearchExpression expr = parser.parse("id:1a.");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("000001", relationalOperation.getValue());
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionCustomOperatorResolver() throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>() {
			{
				put("size", 6);
			}
		};
		OperatorResolver operatorResolver = new DefaultOperatorResolver() {
			@Override
			public SimpleSearchRelationalOperator resolveOperator(Class<?> fieldType, String value, SimpleSearchExpressionField field) {
				String fieldName = field.getName();
				if (fieldName.equals("id")) {
					if (isListOperator(value, fieldType)) {
						return SimpleSearchRelationalOperator.LIST;
					}
				}
				return super.resolveOperator(fieldType, value, field);
			}
		};
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(operatorResolver, new SimpleSearchExpressionField("id", String.class, new StringNumericSearchDataConverter(), params)));
		SimpleSearchExpression expr = parser.parse("id:1,2,3,4,5");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIST, relationalOperation.getOperator());
		assertArrayEquals(asList("000001","000002","000003","000004", "000005").toArray(new String[0]),
				relationalOperation.getValueAsCollection().toArray(new String[0]));
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionInOperatorWithPrimitiveField() throws Exception {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id", int.class)));
		SimpleSearchExpression expr = parser.parse("id:1,2,3,4,5");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIST, relationalOperation.getOperator());
		assertArrayEquals(asList(1, 2, 3, 4, 5).toArray(new Integer[0]),
				relationalOperation.getValueAsCollection().toArray(new Integer[0]));
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionCustomFieldExpr() throws Exception {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name", "metadata.name", String.class)));
		SimpleSearchExpression expr = parser.parse("name:john");

		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals("metadata.name", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("john", relationalOperation.getValue());
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionResolvingDefaultFieldWithDefaultOption() {
		final Map<String, String> defaults = new HashMap<String, String>() {
			{
				put("__default__", "name");
				put("id", "^[0-9]{1,10}$");
			}
		};
		final List<SimpleSearchExpressionField> fields = asList(new SimpleSearchExpressionField("name",String.class), 
				new SimpleSearchExpressionField("id",Integer.class));
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(defaults, fields.toArray(new SimpleSearchExpressionField[0])));
		SimpleSearchExpression expr = parser.parse("john");
		
		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("john", relationalOperation.getValue());
		
		expr = parser.parse("150");
		
		assertEquals(1, expr.getOperations().size());

		relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(new Integer(150), relationalOperation.getValue());
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionResolvingDefaultField() {
		final Map<String, String> defaults = new HashMap<String, String>() {
			{
				put("__default__", "name");
				put("id", "^[0-9]{1,10}$");
			}
		};
		final List<SimpleSearchExpressionField> fields = asList(new SimpleSearchExpressionField("name",String.class), 
				new SimpleSearchExpressionField("id",Integer.class));
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(defaults, fields.toArray(new SimpleSearchExpressionField[0])));
		SimpleSearchExpression expr = parser.parse("12345");
		
		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(new Integer(12345), relationalOperation.getValue());
	}
	
	@Test
	@SuppressWarnings("serial")
	public void shouldParseSimpleSearchExpressionResolvingDefaultFieldMultipleDefaultsConfigured() {
		final Map<String, String> defaults = new HashMap<String, String>() {
			{
				put("__default__", "name");
				put("id", "^[0-9]{1,8}$");
				put("cpfCnpj", "^[0-9]{9,14}$");
			}
		};
		final List<SimpleSearchExpressionField> fields = asList(new SimpleSearchExpressionField("name",String.class), 
				new SimpleSearchExpressionField("id", Integer.class),
				new SimpleSearchExpressionField("cpfCnpj", String.class));
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(defaults, fields.toArray(new SimpleSearchExpressionField[0])));
		SimpleSearchExpression expr = parser.parse("15556615000176");
		
		assertEquals(1, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("cpfCnpj", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("15556615000176", relationalOperation.getValue());
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithSpacesInValueAndOneField() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name", String.class)));
		SimpleSearchExpression expr = parser.parse("name:john galt");

		assertEquals(1, expr.getOperations().size());

		final SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals("name", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("john galt", relationalOperation.getValue());
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionWithSpacesInValueAndMultiplesFields() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("id", Integer.class),
				new SimpleSearchExpressionField("name", String.class), new SimpleSearchExpressionField("age", Integer.class), 
				new SimpleSearchExpressionField("type", String.class)));
		SimpleSearchExpression expr = parser.parse("id:10 name:john galt and dagny taggart age:31 type:(1)sci-fi");
		
		assertEquals(4, expr.getOperations().size());

		SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("id", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(new Integer(10), relationalOperation.getValue());
		
		relationalOperation = expr.getOperations().get(1);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals("name", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("john galt and dagny taggart", relationalOperation.getValue());
		
		relationalOperation = expr.getOperations().get(2);
		assertEquals("age", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals(new Integer(31), relationalOperation.getValue());
		
		relationalOperation = expr.getOperations().get(3);
		assertEquals("type", relationalOperation.getField().getName());
		assertEquals(SimpleSearchRelationalOperator.LIKE, relationalOperation.getOperator());
		assertEquals("(1)sci-fi", relationalOperation.getValue());
	}
	
	@Test
	public void shouldParseSimpleSearchExpressionUsingCustomDefaultOperator() {
		SimpleSearchParser parser = new SimpleSearchParser(new SimpleSearchExpressionPlan(new SimpleSearchExpressionField("name", String.class, SimpleSearchRelationalOperator.EQUALS)));
		SimpleSearchExpression expr = parser.parse("name:john galt");

		assertEquals(1, expr.getOperations().size());

		final SimpleSearchRelationalOperation relationalOperation = expr.getOperations().get(0);
		assertEquals("name", relationalOperation.getField().getName());
		assertEquals("name", relationalOperation.getField().getFieldExpr());
		assertEquals(SimpleSearchRelationalOperator.EQUALS, relationalOperation.getOperator());
		assertEquals("john galt", relationalOperation.getValue());
	}

}
