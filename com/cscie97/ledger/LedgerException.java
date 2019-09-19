package com.cscie97.ledger;

/**
 * Exception thrown by the blockchain ledger
 * Has action and reason properties
 */
class LedgerException extends Exception {
    private String action;
    private String reason;

    LedgerException(String action, String reason) {
        super(action + " error: " + reason);
    }

}
