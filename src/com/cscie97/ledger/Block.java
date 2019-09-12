package com.cscie97.ledger;

import java.util.HashMap;
import java.util.ArrayList;

public class Block {
    private int blockNumber;
    private String previousHash;
    private String hash;
    private ArrayList<Transaction> transactionList;
    private HashMap<String, Account> accountBalanceMap;
    private Block previousBlock;
    private int numberOfTransactions;
    private boolean isFinalized;

    public Block(int blockNumber, String previousHash, String hash, Block previousBlock){
        this.blockNumber = blockNumber;
        this.previousHash = previousHash;
        this.hash = hash;
        this.previousBlock = previousBlock;
        transactionList = new ArrayList<Transaction>();
        accountBalanceMap = new HashMap<String, Account>();
        numberOfTransactions = 0;
        isFinalized = false;
    }

    public Block(){
    }

    public void addTransaction(Transaction transaction){
        transactionList.add(transaction);
        numberOfTransactions += 1;

        if (numberOfTransactions == 10){
            finalizeBlock();
        }
    }

    private void finalizeBlock(){
        // update account balance map here
        isFinalized = true;
    }

    public HashMap<String, Account> getAccountBalanceMap(){
        return accountBalanceMap;
    }

    public void setAccountBalanceMap(HashMap<String, Account> accountBalanceMap){
        this.accountBalanceMap = accountBalanceMap;
    }

    public ArrayList<Transaction> getTransactionList(){
        return transactionList;
    }

    public boolean getIsFinalized(){
        return isFinalized;
    }

    public int getBlockNumber(){
        return blockNumber;
    }

    public String getHash(){
        return hash;
    }

    public String getPreviousHash(){
        return previousHash;
    }

    public Block getPreviousBlock(){
        return previousBlock;
    }
}
