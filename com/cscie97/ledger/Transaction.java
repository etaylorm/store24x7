package com.cscie97.ledger;

/**
 * Represents a single transaction, which is a
 * transfer of funds from one account to another
 * Transactions are validated and processed by
 * the ledger and added to the blocks in batches of 10
 */
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

    /**
     * Getter for the unique identifier for a transaction
     * @return transactionId
     */
    String getTransactionId() {
        return transactionId;
    }

    /**
     * Getter for the payer Account for a transaction
     * @return payer (Account)
     */
    Account getPayer() {
        return payer;
    }

    /**
     * Getter for the receiver Account for a transaction
     * @return receiver (Account)
     */
    Account getReceiver() {
        return receiver;
    }

    /**
     * Getter for the amount of the transaction
     * @return amount
     */
    int getAmount() {
        return amount;
    }

    /**
     * Getter for the fee for the transaction
     * The fee is paid by the payer and transferred to
     * the master account
     * @return fee
     */
    int getFee() {
        return fee;
    }

    /**
     * Overwrites toString method to provide details about
     * the given transaction
     * @return String describing details of transaction
     */
    public String toString(){
        return "Transaction ID: " + transactionId + "; Payer: " + payer.getAddress() + "; Amount: " + amount
                + "; Payload: " + payload + "; Receiver: " + receiver.getAddress() + "; Fee: " + fee;
    }

}
