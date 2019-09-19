package com.cscie97.ledger;

/**
 * Represents an individual account
 * - Uniquely identified by the address of the account
 * - Accounts are created by the ledger and managed
 * by blocks
 * - Account balance cannot be negative
 */
class Account {
    private String address;
    private int balance;

    Account(String address) {
        this.address = address;
        this.balance = 0;  // accounts are initialized with an account balance of 0 by default
    }

    /**
     * Constructor for an Account that allows
     * for an initial balance, overriding
     * the default balance of 0
     * @param address unique identifier for the Account
     * @param balance initial balance
     */
    Account(String address, int balance){
        this.address = address;
        this.balance = balance;
    }

    /**
     * Getter for Account balance
     * @return balance
     */
    int getBalance() {
        return balance;
    }

    /**
     * Getter for Account address
     * (unique identifier for the Account)
     * @return address
     */
    String getAddress() {
        return address;
    }

    /**
     * Updates account balance by adding
     * funds to the current balance
     * @param funds amount by which to change
     *              the Account holder's balance;
     *              can be positive or negative
     */
    void updateBalance(int funds) {
        balance += funds;
    }
}