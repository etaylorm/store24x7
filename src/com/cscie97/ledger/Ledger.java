package com.cscie97.ledger;

import java.util.Base64;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Ledger {
    private String name;
    private String description;
    private String seed;

    private Block genesisBlock;
    private HashMap<String, Block> blockMap;

    private HashMap<String, Account> accounts;
    private Block currentBlock;

    private HashMap<String, Transaction> transactions;

    public Ledger(String name, String description, String seed) throws NoSuchAlgorithmException{
        this.name = name;
        this.description = description;
        this.seed = seed;

        genesisBlock = new Block(1, null, makeHash(seed, 1), new Block());
        currentBlock = genesisBlock;

        blockMap = new HashMap<String, Block>();
        blockMap.put(genesisBlock.getHash(), genesisBlock);

        accounts = new HashMap<String, Account>();

        createAccount("master");
        Account master = accounts.get("master");
        master.updateBalance(Integer.MAX_VALUE);
        accounts.put("master", master);

//        createAccount("evelyn");

//        for(int i = 0; i < 10; i++){
//            processTransaction(new Transaction("1", 0, 0, "test", accounts.get("master"), accounts.get("evelyn")));
//        }

        transactions = new HashMap<String, Transaction>();
    }

    private String createAccount(String accountId){
        Account account = new Account(accountId);
        accounts.put(account.getAddress(), account);

        return accountId;
    }

    private String processTransaction(Transaction transaction) throws NoSuchAlgorithmException {
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

        // update the payer and receiver stored in the accounts map
        Account payer = accounts.get(payer_address);
        Account receiver = accounts.get(receiver_address);

        payer.updateBalance(moneyTransferred * -1);
        receiver.updateBalance(moneyTransferred);

        accounts.put(payer_address, payer);
        accounts.put(receiver_address, receiver);
    }

    private void addBlockToChain() throws NoSuchAlgorithmException {
        try {
            int newBlockNumber = currentBlock.getBlockNumber() + 1;
            Block newBlock = new Block(newBlockNumber, currentBlock.getHash(), makeHash(seed, newBlockNumber), currentBlock);
            blockMap.put(newBlock.getHash(), newBlock);
            currentBlock = newBlock;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
    }

    public int getAccountBalance(String address){
        Block lastCompletedBlock = blockMap.get(currentBlock.getPreviousHash());
        if (lastCompletedBlock != null){
            return lastCompletedBlock.getAccountBalanceMap().get(address);
        } else {
            return 0;
        }
    }

    private HashMap<String, Integer> getAccountBalances(){
        Block lastCompletedBlock = blockMap.get(currentBlock.getPreviousHash());
        return lastCompletedBlock.getAccountBalanceMap();
    }

    private Block getBlock(String blockHash){
        return blockMap.get(blockHash);
    }

    private Transaction getTransaction(String transactionId){
        return transactions.get(transactionId);
    }

    private void validate(){
        // starting with current block and looping backwards
            // check that account balances equal max value

            // check that completed blocks have 10 transactions

            // check that we end up back at the genesis block
    }

    private String makeHash(String seed, int blockNumber) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String text = seed + blockNumber;
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
