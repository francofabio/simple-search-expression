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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleSearchExpressionPlan {
	private static final String DEFAULT_FIELD_KEY = "__default__";
	
	private OperatorResolver operatorResolver;
	private Map<String, SimpleSearchExpressionField> fields;
	private Map<String, String> defaults;
	
	public SimpleSearchExpressionPlan() {
		this.operatorResolver = new DefaultOperatorResolver();
		this.fields = new HashMap<String, SimpleSearchExpressionField>();
		this.defaults = new HashMap<String, String>();
	}
	
	public SimpleSearchExpressionPlan(SimpleSearchExpressionField...fields) {
		this();
		for (SimpleSearchExpressionField field : fields) {
			this.fields.put(field.getName(), field);
		}
	}
	
	public SimpleSearchExpressionPlan(OperatorResolver operatorResolver, SimpleSearchExpressionField...fields) {
		this(fields);
		this.operatorResolver = operatorResolver;
	}
	
	public SimpleSearchExpressionPlan(Map<String, String> defaults, SimpleSearchExpressionField...fields) {
		this(fields);
		this.defaults.putAll(defaults);
	}
	
	public SimpleSearchExpressionPlan(OperatorResolver operatorResolver, Map<String, String> defaults, SimpleSearchExpressionField...fields) {
		this(defaults, fields);
		this.operatorResolver = operatorResolver;
	}
	
	public OperatorResolver getOperatorResolver() {
		return operatorResolver;
	}
	
	public Collection<SimpleSearchExpressionField> getFields() {
		return fields.values();
	}
	
	public SimpleSearchExpressionField addField(SimpleSearchExpressionField field) {
		this.fields.put(field.getName(), field);
		return field;
	}
	
	public SimpleSearchExpressionField getField(String name) {
		return this.fields.get(name);
	}
	
	public Map<String, String> getDefaults() {
		return defaults;
	}
	
	public void addDefaultExpression(String fieldName, String regex) {
		if (!this.defaults.containsKey(fieldName)) {
			this.defaults.put(fieldName, regex);
		}
	}
	
	public SimpleSearchExpressionField getAppropriateFieldByAnonymousExpression(String anonymousSearchExpr) {
		Set<String> names = this.defaults.keySet();
		for (String fieldName : names) {
			if (!fieldName.equals(DEFAULT_FIELD_KEY)) {
				if (Pattern.matches(this.defaults.get(fieldName), anonymousSearchExpr)) {
					return getField(fieldName);
				}
			}
		}
		if (this.defaults.containsKey(DEFAULT_FIELD_KEY)) {
			final String defaultFieldName = this.defaults.get(DEFAULT_FIELD_KEY);
			return getField(defaultFieldName);
		}
		return null;
	}
	
}
