package com.cscie97.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.ArrayList;

class Block {
    private int blockNumber;
    private String previousHash;
    private String hash;
    private ArrayList<Transaction> transactionList;
    private HashMap<String, Account> accountBalanceMap;
    private Block previousBlock;

    Block(int blockNumber, String previousHash, Block previousBlock, HashMap<String, Account> accountBalanceMap){
        this.blockNumber = blockNumber;
        this.previousHash = previousHash;
        this.previousBlock = previousBlock;
        this.accountBalanceMap = accountBalanceMap;
        transactionList = new ArrayList<Transaction>();
    }

    Block(){ }

    void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    HashMap<String, Account> getAccountBalanceMap(){
        return accountBalanceMap;
    }

    void setAccountBalanceMap(HashMap<String, Account> accountBalanceMap){
        this.accountBalanceMap = accountBalanceMap;
    }

    ArrayList<Transaction> getTransactionList(){
        return transactionList;
    }

    int getBlockNumber(){
        return blockNumber;
    }

    String getHash(){
        return hash;
    }

    void setHash(String hash){
        this.hash = hash;
    }

    String getPreviousHash(){
        return previousHash;
    }

    Block getPreviousBlock(){
        return previousBlock;
    }

    void validate() throws LedgerException {
        int accountSum = 0;
        for (Account account : accountBalanceMap.values()){
            accountSum += account.getBalance();
        }
        if (accountSum != Integer.MAX_VALUE | transactionList.size() != 10){
            throw new LedgerException("validate", "block is not valid");
        }
    }

}
