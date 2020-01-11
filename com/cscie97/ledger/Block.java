package com.cscie97.ledger;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Represents a single block within the blockchain
 * - Holds 10 transactions once committed
 * - Manages a list of Accounts and balances
 * - Contains a reference to the previous block
 * - Is immutable after being committed
 */
class Block {
    private int blockNumber;
    private String previousHash;
    private String hash;
    private ArrayList<Transaction> transactionList;
    private HashMap<String, Account> accountBalanceMap;
    private HashMap<String, Account> pendingAccountBalanceMap;
    private Block previousBlock;

    Block(int blockNumber, String previousHash, Block previousBlock, HashMap<String, Account> accountBalanceMap,
          HashMap<String, Account> pendingAccountBalanceMap){
        this.blockNumber = blockNumber;
        this.previousHash = previousHash;
        this.previousBlock = previousBlock;
        this.accountBalanceMap = accountBalanceMap;
        this.pendingAccountBalanceMap = pendingAccountBalanceMap;
        transactionList = new ArrayList<Transaction>();
    }

    Block(){ }  // the genesis points to a null block as its previous block

    /**
     * Adds a transaction to the Block's list of transactions
     * @param transaction pre-validated record of payment between two Accounts
     */
    void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    /**
     * Getter for the map of addresses to accounts
     * @return accountBalanceMap
     */
    HashMap<String, Account> getAccountBalanceMap(){
        return accountBalanceMap;
    }

    /**
     * Getter for the list of transactions within the block
     * @return transactionList
     */
    ArrayList<Transaction> getTransactionList(){
        return transactionList;
    }

    /**
     * Getter for the unique block number of this block
     * @return blockNumber
     */
    int getBlockNumber(){
        return blockNumber;
    }

    /**
     * Getter for the hash of this block, computed
     * when the block is finalized
     * @return
     */
    String getHash(){
        return hash;
    }

    /**
     * Setter for the block's hash
     * @param hash SHA-256 hash based on all elements of the block
     *             in addition to unique characteristics of the ledger
     *             when the Block is ready to be committed
     */
    void setHash(String hash){
        this.hash = hash;
    }

    /**
     * Getter for the hash of the previous block
     * @return previousHash
     */
    String getPreviousHash(){
        return previousHash;
    }

    /**
     * Getter for the reference to a previous block
     * @return previousBlock
     */
    Block getPreviousBlock(){
        return previousBlock;
    }

    /**
     * Validates the block based on two criteria:
     * 1. Block has 10 transactions
     * 2. The total balance in all accounts adds to the initial value
     * of the blockchain (Integer.MAX_VALUE)
     * @throws LedgerException if the block is not valid, it throws an exception
     */
    void validate() throws LedgerException {
        int accountSum = 0;
        for (Account account : accountBalanceMap.values()){
            accountSum += account.getBalance();
        }
        if (accountSum != Integer.MAX_VALUE | transactionList.size() != 1){
            throw new LedgerException("validate", "block is not valid");
        }
    }

    /**
     * Informational string intended to be displayed to users
     * @return String with key facts about the block
     */
    String getInfoString(){
        return "Block number: " + blockNumber + "; Number of transactions: " + transactionList.size()
                + " Transactions: " + transactionList;
    }

    /**
     * Method to add Accounts to the collection of Accounts, updated with their
     * pending balances based on transactions within this block
     * This method is used for new Accounts created while the block is unfinalized
     * @param account Account to be added to the pending account map
     */
    void addAccountPendingAccountBalanceMap(Account account){
        pendingAccountBalanceMap.put(account.getAddress(), account);
    }

    /**
     * Getter for the map of Accounts updated with pending balances
     * for uncommitted transactions contained within this block
     * @return pendingAccountBalanceMap
     */
    HashMap<String, Account> getPendingAccountBalanceMap(){
        return pendingAccountBalanceMap;
    }

    /**
     * Setter for the map of Accounts containing only committed transactions
     * While the block is uncommitted, the accountBalanceMap contains all
     * transactions from the previous block.
     * When the block is committed, this method is used to update the map
     * of Accounts for this block with all now-committed transactions contained
     * within this block
     * @param accountBalanceMap map of Accounts
     */
    void setAccountBalanceMap(HashMap<String, Account> accountBalanceMap){
        this.accountBalanceMap = accountBalanceMap;
    }
}
