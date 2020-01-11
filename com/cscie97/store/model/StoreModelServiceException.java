package com.cscie97.store.model;

/**
 * Exception class for the store model service
 */
public class StoreModelServiceException extends Exception {
	private String action;
	private String error;

	StoreModelServiceException(String action, String error) {
		super(action + " error: " + error);
		this.action = action;
		this.error = error;
	}

	/**
	 * Getter for the action that caused the error
	 * @return action that caused error
	 */
	public String getAction(){
		return action;
	}

	/**
	 * Getter for the specific type of error raised
	 * @return error details
	 */
	public String getError(){
		return error;
	}
}
