package com.cscie97.ledger;

class LedgerException extends Exception {
    private String action;
    private String reason;

    LedgerException(String action, String reason) {
        super(action + " error: " + reason);
    }

    String getAction(){
        return action;
    }

    String getReason(){
        return reason;
    }
}
