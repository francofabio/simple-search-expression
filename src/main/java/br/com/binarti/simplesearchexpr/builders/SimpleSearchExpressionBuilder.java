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
package br.com.binarti.simplesearchexpr.builders;

import br.com.binarti.simplesearchexpr.SimpleSearchExpression;

/**
 * Interface for simple search expression builder.<br/>
 * This interface defines a builder for simple expression search to use with a database.
 * 
 * @author francofabio
 *
 * @param <R> The result type of the builder 
 */
public interface SimpleSearchExpressionBuilder<R> {

	/**
	 * Build the search command to use with the database.
	 *  
	 * @param searchExpression The simple search expressions
	 * @return The search command to use with the database
	 */
	R build(SimpleSearchExpression searchExpression);
	
}
