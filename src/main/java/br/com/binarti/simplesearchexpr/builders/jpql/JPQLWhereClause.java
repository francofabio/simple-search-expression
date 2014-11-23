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

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

/**
 * Represents the JPQL where clause data
 * 
 * @author francofabio
 */
public class JPQLWhereClause {

	private StringBuilder whereClause;
	private Map<String, Object> parameters;
	
	public JPQLWhereClause() {
		this.whereClause = new StringBuilder();
		this.parameters = new HashMap<String, Object>();
	}
	
	/**
	 * Add a expression to where clause
	 * @param expr The expression
	 * @param paramName The parameter name to store value of the expression.
	 * @param paramValue The value of the parameter
	 */
	public void add(String expr, String paramName, Object paramValue) {
		if (whereClause.length() > 0) {
			whereClause.append(" and ");
		}
		whereClause.append(expr);
		parameters.put(paramName, paramValue);
	}
	
	/**
	 * Gets the where clause
	 * @return The where clause
	 */
	public String getWhereClause() {
		return whereClause.toString();
	}
	
	/**
	 * Gets the parameters to use in where clause expression
	 * @return The where clause parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	/**
	 * Join the JPQL where clause with a JPQL from clause
	 * 
	 * @param jpqlFrom The JPQL from clause to join with where clause
	 * @return The joined JPQL from and where clauses
	 */
	public String join(String jpqlFrom) {
		StringBuilder jpql = new StringBuilder(jpqlFrom);
		String whereClause = getWhereClause();
		if (!isEmpty(whereClause)) {
			jpql.append(" where ").append(whereClause);
		}
		return jpql.toString();
	}
	
	/**
	 * Apply parameter values to a query.
	 * @param query The query to receive parameter values
	 */
	public void applyParameters(Query query) {
		if (parameters.size() > 0) {
			for (Entry<String, Object> param : parameters.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
	}
	
}
