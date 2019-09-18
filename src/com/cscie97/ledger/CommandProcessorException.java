package com.cscie97.ledger;

public class CommandProcessorException extends Exception{
    private String command;
    private String reason;

    CommandProcessorException(String command, String reason) {
        super(command + " error: " + reason);
        this.command = command;
        this.reason = reason;
    }

    CommandProcessorException(String command, String reason, int lineNumber){
        super(command + " error: " + reason + " at line " + lineNumber);
        this.command = command;
        this.reason = reason;
    }

    String getReason(){
        return reason;
    }

    String getCommand() {
        return command;
    }
}

