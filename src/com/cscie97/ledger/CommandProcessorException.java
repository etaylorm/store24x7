package com.cscie97.ledger;

class CommandProcessorException extends Exception{
    CommandProcessorException(String command, String reason) {
        super(command + " error: " + reason);
    }

    CommandProcessorException(String command, String reason, int lineNumber){
        super(command + " error: " + reason + " at line " + lineNumber);
    }

}

