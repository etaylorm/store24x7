package com.cscie97.ledger;

class Account {
    private String address;
    private int balance;

    Account(String address) {
        this.address = address;
        this.balance = 0;
    }

    Account(String address, int balance){
        this.address = address;
        this.balance = balance;
    }

    int getBalance() {
        return balance;
    }

    String getAddress() {
        return address;
    }

    void updateBalance(int funds) {
        balance += funds;
    }
}