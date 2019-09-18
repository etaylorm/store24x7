package com.cscie97.ledger;

class Transaction {
    private String transactionId;
    private int amount;
    private int fee;
    private String payload;
    private Account payer;
    private Account receiver;

    Transaction(String transactionId, int amount, int fee, String payload, Account payer, Account receiver) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.payload = payload;
        this.payer = payer;
        this.receiver = receiver;
    }

    String getTransactionId() {
        return transactionId;
    }

    Account getPayer() {
        return payer;
    }

    Account getReceiver() {
        return receiver;
    }

    int getAmount() {
        return amount;
    }

    int getFee() {
        return fee;
    }

    public String toString(){
        return "Transaction ID: " + transactionId + "; Payer: " + payer.getAddress() + "; Amount: " + amount
                + "; Payload: " + payload + "; Receiver: " + receiver.getAddress() + "; Fee: " + fee;
    }

}
