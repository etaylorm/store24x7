package com.cscie97.ledger;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Ledger {
    private String name;
    private String description;
    private String seed;
    private HashMap<String, Block> blockMap;
    private Block genesisBlock;
    private Block currentBlock;

    public Ledger(String name, String description, String seed) throws NoSuchAlgorithmException{
        this.name = name;
        this.description = description;
        this.seed = seed;
        blockMap = new HashMap<String, Block>();

        createGenesisBlock();
        initializeMasterAccount();
    }

    private void createGenesisBlock() throws NoSuchAlgorithmException {
        genesisBlock = new Block(1, null, makeHash(seed, 1, name, description), new Block());
        currentBlock = genesisBlock;
        blockMap.put(genesisBlock.getHash(), genesisBlock);
    }

    private void initializeMasterAccount(){
        createAccount("master");
        HashMap<String, Account> accountBalancesMap = currentBlock.getAccountBalanceMap();
        Account master = accountBalancesMap.get("master");
        master.updateBalance(Integer.MAX_VALUE);
        accountBalancesMap.put("master", master);

        currentBlock.setAccountBalanceMap(accountBalancesMap);
    }

    public String createAccount(String accountId){
        Account account = new Account(accountId);
        HashMap<String, Account> accountBalancesMap = currentBlock.getAccountBalanceMap();
        accountBalancesMap.put(accountId, account);
        currentBlock.setAccountBalanceMap(accountBalancesMap);
        return accountId;
    }

    public String processTransaction(Transaction transaction) throws NoSuchAlgorithmException {
        // add code to validate transaction

        currentBlock.addTransaction(transaction);
        updateAccounts(transaction);

        if (currentBlock.getIsFinalized()){
            addBlockToChain();
        }

        return transaction.getTransactionId();
    }

    private void updateAccounts(Transaction transaction){
        // get the amount of money that changed hands: amount + fee
        int moneyTransferred = transaction.getAmount() + transaction.getFee();

        // get the unique identifiers for the payer and receiver
        String payer_address = transaction.getPayer().getAddress();
        String receiver_address = transaction.getReceiver().getAddress();

        HashMap<String, Account> accountBalancesMap = currentBlock.getAccountBalanceMap();
        // update the payer and receiver stored in the accounts map
        Account payer = accountBalancesMap.get(payer_address);
        Account receiver = accountBalancesMap.get(receiver_address);

        payer.updateBalance(moneyTransferred * -1);
        receiver.updateBalance(moneyTransferred);

        accountBalancesMap.put(payer_address, payer);
        accountBalancesMap.put(receiver_address, receiver);

        currentBlock.setAccountBalanceMap(accountBalancesMap);
    }

    private void addBlockToChain() throws NoSuchAlgorithmException {
        try {
            int newBlockNumber = currentBlock.getBlockNumber() + 1;
            HashMap<String, Account> currentAccountBalances = currentBlock.getAccountBalanceMap();
            Block newBlock = new Block(newBlockNumber, currentBlock.getHash(), makeHash(seed, newBlockNumber, name, description), currentBlock);
            blockMap.put(newBlock.getHash(), newBlock);
            newBlock.setAccountBalanceMap(currentAccountBalances);
            currentBlock = newBlock;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
    }

    public int getAccountBalance(String address){
        Block lastCompletedBlock = blockMap.get(currentBlock.getPreviousHash());
        if (lastCompletedBlock != null){
            return lastCompletedBlock.getAccountBalanceMap().get(address).getBalance();
        } else {
            return 0;
        }
    }

    private HashMap<String, Account> getAccountBalances(){
        Block lastCompletedBlock = blockMap.get(currentBlock.getPreviousHash());
        return lastCompletedBlock.getAccountBalanceMap();
    }

    public Block getBlock(String blockHash){
        return blockMap.get(blockHash);
    }

    public Transaction getTransaction(String transactionId){
        return getTransactionHelper(transactionId, currentBlock);
    }

    private Transaction getTransactionHelper(String transactionId, Block thisBlock){
        ArrayList<Transaction> transactionList = thisBlock.getTransactionList();
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).getTransactionId().equals(transactionId)) {
                return transactionList.get(i);
            }
        }
        if (thisBlock.equals(genesisBlock)) {
            return null;
        } else {
            return getTransactionHelper(transactionId, thisBlock.getPreviousBlock());
        }
    }

    private void validate(){
        // starting with current block and looping backwards
            // check that account balances equal max value

            // check that completed blocks have 10 transactions

            // check that we end up back at the genesis block
    }

    private String makeHash(String seed, int blockNumber, String name, String description) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String text = seed + blockNumber + name + description;
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
