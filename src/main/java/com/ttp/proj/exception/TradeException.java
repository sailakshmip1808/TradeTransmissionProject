/**
 * Custom Exception class for handling all the trade related exceptions
 */
package com.ttp.proj.exception;

/**
 * @author Sailakshmi Pakkala
 *
 */
public class TradeException extends Exception {

	private static final long serialVersionUID = -6313124267656152881L;

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public TradeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 */
	public TradeException(String message) {
		super(message);
	}
}
