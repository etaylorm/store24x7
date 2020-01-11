package com.cscie97.ledger;

/**
 * Exception thrown by the blockchain ledger
 * Has action and reason properties
 */
public class LedgerException extends Exception {
    private String action;
    private String reason;

    LedgerException(String action, String reason) {
        super(action + " error: " + reason);
        this.action = action;
        this.reason = reason;
    }

    public String getAction(){
        return action;
    }

    public String getReason(){
        return reason;
    }

}
