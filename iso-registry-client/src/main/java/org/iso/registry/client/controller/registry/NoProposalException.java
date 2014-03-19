/**
 * 
 */
package org.iso.registry.client.controller.registry;

import java.util.UUID;

/**
 * @author Florian.Esser
 *
 */
public class NoProposalException extends Exception
{

	/**
	 * 
	 */
	public NoProposalException() {
		// TODO Auto-generated constructor stub
	}

	public NoProposalException(UUID itemId) {
		super(String.format("The item with ID '%s' is not a proposal.", itemId));
	}
	/**
	 * @param message
	 */
	public NoProposalException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoProposalException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoProposalException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoProposalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
