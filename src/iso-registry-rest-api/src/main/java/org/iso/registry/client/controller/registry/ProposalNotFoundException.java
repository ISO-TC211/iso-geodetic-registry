/**
 * 
 */
package org.iso.registry.client.controller.registry;

import java.util.UUID;

/**
 * @author Florian.Esser
 *
 */
public class ProposalNotFoundException extends Exception
{

	/**
	 * 
	 */
	public ProposalNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public ProposalNotFoundException(UUID pmiId) {
		super(String.format("A proposal management information with ID '%s' does not exist.", pmiId));
	}

	/**
	 * @param message
	 */
	public ProposalNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ProposalNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProposalNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ProposalNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
