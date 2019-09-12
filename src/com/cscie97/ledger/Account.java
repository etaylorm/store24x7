package com.cscie97.ledger;

public class Account {
    private String address;
    private int balance;

    public Account(String address) {
        this.address = address;
        this.balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public void updateBalance(int funds) {
        balance += funds;
    }
}
