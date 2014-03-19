/**
 * 
 */
package org.iso.registry.client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception can be thrown by request handlers to indicate
 * that the requested resource could not be found. Throwing this
 * exception results in a HTTP 404 result.
 * 
 * @author Florian Esser
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException
{

	/**
	 * 
	 */
	public NotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
