package com.cscie97.ledger;

/**
 * Public interface for the blockchain ledger. The ledger is used by the store controller service to
 * record all transactions within the 24x7 stores
 */
public interface LedgerInterface {
    String createAccount(String accountId) throws LedgerException;
    int getAccountBalance(String accountId) throws LedgerException;
    void addTransaction(String transactionId,
                        int amount,
                        int fee,
                        String payload,
                        String payerId,
                        String receiverId) throws LedgerException;
}
