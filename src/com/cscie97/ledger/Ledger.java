package com.cscie97.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Ledger {
    private String name;
    private String description;
    private String seed;
    private HashMap<Integer, Block> blockMap;
    private Block genesisBlock;
    private Block currentBlock; // is this permitted?
    private HashMap<String, Integer> accountMap;

    Ledger(String name, String description, String seed) {
        this.name = name;
        this.description = description;
        this.seed = seed;
        blockMap = new HashMap<Integer, Block>();
        accountMap = new HashMap<String, Integer>();

        createGenesisBlock();
        createMasterAccount();
    }

    private void createGenesisBlock() {
        genesisBlock = new Block(0, null, new Block(), new HashMap<String, Account>());
        blockMap.put(genesisBlock.getBlockNumber(), genesisBlock);
        currentBlock = genesisBlock;
    }

    private void createMasterAccount(){
        Account master = new Account("master");
        accountMap.put("master", Integer.MAX_VALUE);
    }

    String createAccount(String accountId) throws LedgerException {
        if (!accountMap.containsKey(accountId)) {
            // create new account
            accountMap.put(accountId, 0);
            return accountId;
        } else {
            throw new LedgerException("create-account", "account already exists");
        }
    }

    String processTransaction(Transaction transaction) throws LedgerException {
        // validate transaction first
        String payerAddress = transaction.getPayer().getAddress();
        String receiverAddress = transaction.getReceiver().getAddress();

        try {
            getTransaction(transaction.getTransactionId());
            throw new LedgerException("process-transaction", "transaction ID already exists");
        } catch (LedgerException l){ }

        // check that both accounts exist
        if (accountMap.get(payerAddress) == null
            & accountMap.get(receiverAddress) == null) {
            throw new LedgerException("process-transaction", "account does not exist");
        }
        // check that the payer has enough cash for the amount and the fee
        else if (accountMap.get(payerAddress) < transaction.getAmount() + transaction.getFee()) {
            throw new LedgerException("process-transaction", "payer account does not have sufficient funds");
        }
        // check that the fee is greater than 10
        else if (transaction.getFee() < 10) {
            throw new LedgerException("process-transaction", "fee must be more than 10");
        }

        // if the transaction is valid, update payer, receiver, and master accounts
        else {
            accountMap.put(payerAddress, accountMap.get(payerAddress) - (transaction.getAmount() + transaction.getFee()));
            accountMap.put(receiverAddress, accountMap.get(receiverAddress) + transaction.getAmount());
            accountMap.put("master", accountMap.get("master") + transaction.getFee());
        }

        currentBlock.addTransaction(transaction);

        if (currentBlock.getTransactionList().size() == 10){
            addBlockToChain();
        }

        return transaction.getTransactionId();
    }

    private void addBlockToChain() throws LedgerException {
        // close out current block by setting the hash
        currentBlock.setHash(makeHash(seed, currentBlock.getBlockNumber(), currentBlock.getPreviousHash(),
                currentBlock.getPreviousBlock(), currentBlock.getTransactionList(),
                currentBlock.getAccountBalanceMap()));

        // update the account balance map according to the ledger's account balance map
        HashMap<String, Account> updatedAccountBalanceMap = updateAccountBalances();

        // create a new block
        int newBlockNumber = currentBlock.getBlockNumber() + 1;
        Block newBlock = new Block(newBlockNumber, currentBlock.getHash(), currentBlock, updatedAccountBalanceMap);

        blockMap.put(newBlockNumber, newBlock);
        currentBlock = newBlock;
    }

    private HashMap<String, Account> updateAccountBalances() {
        HashMap<String, Account> updatedAccountBalanceMap = new HashMap<String, Account>();

        for (String accountId : accountMap.keySet()) {
            Account newAccount = new Account(accountId);
            newAccount.updateBalance(accountMap.get(accountId));
            updatedAccountBalanceMap.put(accountId, newAccount);
        }
        return updatedAccountBalanceMap;
    }

    // should the account balance map be copied in at the beginning of making the block?
    int getAccountBalance(String address) throws LedgerException {
        try {
            return currentBlock.getAccountBalanceMap().get(address).getBalance();
        }
        catch (NullPointerException e) {
            throw new LedgerException("get-account-balance", "account has no committed balance");
        }
    }

    // comment above all relevant here
    HashMap<String, Account> getAccountBalances() throws LedgerException {
        if (currentBlock != genesisBlock) {
            return currentBlock.getAccountBalanceMap();
        }
        else {
            throw new LedgerException("get-account-balances", "no balances have been committed");
        }
    }

    Block getBlock(int blockNumber){
        return blockMap.get(blockNumber);
    }

    Transaction getTransaction(String transactionId) throws LedgerException {
        Transaction transaction = getTransactionHelper(transactionId, currentBlock);
        if (transaction == null) {
            throw new LedgerException("get-transaction", "no transaction with that ID found");
        }
        else {
            return transaction;
        }
    }

    private Transaction getTransactionHelper(String transactionId, Block thisBlock){
        ArrayList<Transaction> transactionList = thisBlock.getTransactionList();
        for (Transaction transaction : transactionList) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        if (thisBlock.equals(genesisBlock)) {
            return null;
        } else {
            return getTransactionHelper(transactionId, thisBlock.getPreviousBlock());
        }
    }

    void validate() throws LedgerException {
        validateHelper(currentBlock.getPreviousBlock());
    }

    private void validateHelper(Block thisBlock) throws LedgerException {
        if (!thisBlock.equals(genesisBlock)){
            thisBlock.validate();
            validateHelper(thisBlock.getPreviousBlock());
        }
    }

    private String makeHash(String seed, int blockNumber, String previousHash, Block previousBlock,
                            ArrayList<Transaction> transactions, HashMap<String, Account> accountBalanceMap)
            throws LedgerException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String text = seed + blockNumber + previousHash + previousBlock + transactions + accountBalanceMap;
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new LedgerException("process-transaction", "unable to create new block");
        }
    }
}
