package com.cscie97.ledger;

public class Transaction {
    private String transactionId;
    private int amount;
    private int fee;
    private String payload;
    private Account payer;
    private Account receiver;

    public Transaction(String transactionId, int amount, int fee, String payload, Account payer, Account receiver) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.payload = payload;
        this.payer = payer;
        this.receiver = receiver;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Account getPayer(){
        return payer;
    }

    public Account getReceiver(){
        return receiver;
    }

    public int getAmount(){
        return amount;
    }

    public int getFee(){
        return fee;
    }

}
