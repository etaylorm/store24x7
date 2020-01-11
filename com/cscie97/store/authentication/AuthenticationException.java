package com.cscie97.store.authentication;

/**
 * Abstract class for authentication exceptions. Contains getters for action and error
 * so that exceptions can be rolled up into other exceptions if needed
 */
abstract class AuthenticationException extends Exception {
	private String action;
	private String error;

	AuthenticationException(String action, String error){
		this.action = action;
		this.error = error;
	}

	/**
	 * Getter for the action that caused the error
	 * @return action that caused the error
	 */
	public String getAction(){
		return action;
	}

	/**
	 * Getter for the specific error thrown
	 * @return error that was raised
	 */
	public String getError(){
		return error;
	}

}
