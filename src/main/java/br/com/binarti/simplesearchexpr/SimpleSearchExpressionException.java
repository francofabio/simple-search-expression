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

@SuppressWarnings("serial")
public class SimpleSearchExpressionException extends RuntimeException {

	public SimpleSearchExpressionException() {
	}

	public SimpleSearchExpressionException(String message) {
		super(message);
	}

	public SimpleSearchExpressionException(Throwable cause) {
		super(cause);
	}

	public SimpleSearchExpressionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SimpleSearchExpressionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
