package com.cscie97.ledger;

/**
 * CommandProcessorException is thrown by the CommandProcessor
 * that parses commands by file or as strings and passes them
 * to the blockchain ledger
 * Has command, reason, and line number properties
 */
public class CommandProcessorException extends Exception{
    private String command;
    private String reason;

    CommandProcessorException(String command, String reason) {
        super(command + " error: " + reason);
        this.command = command;
        this.reason = reason;
    }

    /**
     * Constructor for exceptions thrown when processing a file
     * of commands
     * @param command command that caused the exception
     * @param reason the reason for the exception
     * @param lineNumber the line number for the command within
     *                   the file
     */
    CommandProcessorException(String command, String reason, int lineNumber){
        super(command + " error: " + reason + " at line " + lineNumber);
        this.command = command;
        this.reason = reason;
    }

    /**
     * Getter for the reason
     * @return reason
     */
    String getReason(){
        return reason;
    }

    /**
     * Getter for the command
     * @return command
     */
    String getCommand() {
        return command;
    }
}

