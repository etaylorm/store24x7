package com.cscie97.store.model;

/**
 * CommandProcessorException is thrown by the CommandProcessor
 * Has command, reason, and line number properties
 */
public class CommandProcessorException extends Exception{
	private String action;
	private String error;

	CommandProcessorException(String action, String error) {
		super(action + " error: " + error);
		this.action = action;
		this.error = error;
	}

	/**
	 * Constructor for exceptions thrown when processing a file
	 * of commands
	 * @param action action that caused the exception
	 * @param error the reason for the exception
	 * @param lineNumber the line number for the command within
	 *                   the file
	 */
	CommandProcessorException(String action, String error, int lineNumber){
		super(action + " error: " + error + " at line " + lineNumber);
		this.action = action;
		this.error = error;
	}

	/**
	 * Getter for the reason
	 * @return error text
	 */
	String getError(){
		return error;
	}

	/**
	 * Getter for the command
	 * @return command that caused the rror
	 */
	String getAction() {
		return action;
	}
}

