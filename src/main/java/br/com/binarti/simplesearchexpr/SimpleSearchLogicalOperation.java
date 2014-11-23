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
import java.util.List;

public class SimpleSearchLogicalOperation {

	private final SimpleSearchLogicalOperator operator;
	private final List<SimpleSearchRelationalOperation> operations;
	
	public SimpleSearchLogicalOperation(SimpleSearchLogicalOperator operator) {
		this.operator = operator;
		this.operations = new ArrayList<SimpleSearchRelationalOperation>();
	}
	
	public SimpleSearchLogicalOperator getOperator() {
		return operator;
	}
	
	public List<SimpleSearchRelationalOperation> getOperations() {
		return operations;
	}
	
	public void addRelationalOperation(SimpleSearchRelationalOperation operation) {
		this.operations.add(operation);
	}
	
}
