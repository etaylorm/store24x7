package com.cscie97.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.security.NoSuchAlgorithmException;

class Ledger {
    private String name;
    private String description;
    private String seed;
    private HashMap<Integer, Block> blockMap;
    private Block genesisBlock;
    private Block currentBlock;

    Ledger(String name, String description, String seed) {
        this.name = name;
        this.description = description;
        this.seed = name;
        blockMap = new HashMap<>();

        genesisBlock = new Block(0, null, new Block(), new HashMap<>());
        blockMap.put(genesisBlock.getBlockNumber(), genesisBlock);
        currentBlock = genesisBlock;

        Account master = new Account("master", Integer.MAX_VALUE);
        currentBlock.addAccountPendingAccountBalanceMap(master);
    }

    String createAccount(String accountId) throws LedgerException {
        if (currentBlock.getPendingAccountBalanceMap().get(accountId) == null) {
            // create new account
            Account account = new Account(accountId);
            currentBlock.addAccountPendingAccountBalanceMap(account);
            return accountId;
        } else {
            throw new LedgerException("create-account", "account already exists");
        }
    }

    String processTransaction(Transaction transaction) throws LedgerException {
        // validate transaction first
        String payerAddress = transaction.getPayer().getAddress();
        String receiverAddress = transaction.getReceiver().getAddress();

        Transaction duplicateTransactionId = getTransaction(transaction.getTransactionId());

        if (duplicateTransactionId != null){
            throw new LedgerException("process-transaction", "transaction ID already exists");
        }

        // check that both accounts exist
        if (!currentBlock.getPendingAccountBalanceMap().containsKey(payerAddress)
                | !currentBlock.getPendingAccountBalanceMap().containsKey(payerAddress)) {
            throw new LedgerException("process-transaction", "account does not exist");
        }

        Account payer = currentBlock.getPendingAccountBalanceMap().get(payerAddress);
        Account receiver = currentBlock.getPendingAccountBalanceMap().get(receiverAddress);
        Account master = currentBlock.getAccountBalanceMap().get("master");

        // check that the payer has enough cash for the amount and the fee
        if (payer.getBalance() < transaction.getAmount() + transaction.getFee()) {
            throw new LedgerException("process-transaction", "payer account does not have sufficient funds");
        }
        // check that the fee is greater than 10
        if (transaction.getFee() < 10) {
            throw new LedgerException("process-transaction", "fee must be more than 10");
        }

        // if the transaction is valid, update payer, receiver, and master accounts
        payer.updateBalance(-1 * (transaction.getAmount() + transaction.getFee()));
        receiver.updateBalance(transaction.getAmount());
        master.updateBalance(transaction.getFee());

        currentBlock.addTransaction(transaction);

        if (currentBlock.getTransactionList().size() == 10){
            addBlockToChain();
        }

        return transaction.getTransactionId();
    }

    private void addBlockToChain() throws LedgerException {
        // close out current block by setting the hash
        currentBlock.setHash(makeHash(currentBlock.getBlockNumber(), currentBlock.getPreviousHash(),
                currentBlock.getPreviousBlock(), currentBlock.getTransactionList(),
                currentBlock.getAccountBalanceMap()));

        currentBlock.setAccountBalanceMap(currentBlock.getPendingAccountBalanceMap());

        // update the account balance map according to the ledger's account balance map
        HashMap<String, Account> accountBalancesMap = copyAccountBalanceMap(currentBlock.getAccountBalanceMap());

        // create a new block
        int newBlockNumber = currentBlock.getBlockNumber() + 1;
        Block newBlock = new Block(newBlockNumber, currentBlock.getHash(), currentBlock, accountBalancesMap);

        blockMap.put(newBlockNumber, newBlock);
        currentBlock = newBlock;
    }

    private HashMap<String, Account> copyAccountBalanceMap(HashMap<String, Account> map) {
        HashMap<String, Account> copyOfAccountBalanceMap = new HashMap<>();

        for (String accountId : map.keySet()) {
            copyOfAccountBalanceMap.put(accountId, new Account(accountId, map.get(accountId).getBalance()));
        }
        return copyOfAccountBalanceMap;
    }

    int getAccountBalance(String address) throws LedgerException {
        try {
            return currentBlock.getAccountBalanceMap().get(address).getBalance();
        }
        catch (NullPointerException e) {
            throw new LedgerException("get-account-balance", "account has no committed balance");
        }
    }

    HashMap<String, Integer> getAccountBalances() {
        HashMap<String, Integer> balanceMap = new HashMap<>();
        HashMap<String, Account> accountBalanceMap = currentBlock.getAccountBalanceMap();

        for (String accountId : accountBalanceMap.keySet()) {
            balanceMap.put(accountId, accountBalanceMap.get(accountId).getBalance());
        }
        return balanceMap;
    }

    Block getBlock(int blockNumber){
        return blockMap.get(blockNumber);
    }

    Transaction getTransaction(String transactionId) {
        return getTransactionHelper(transactionId, currentBlock);
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
        validateHelper(currentBlock);
    }

    private void validateHelper(Block block) throws LedgerException {
        if (!block.equals(genesisBlock)) {
            Block previousBlock = block.getPreviousBlock();
            previousBlock.validate();
            validateHash(previousBlock.getPreviousBlock(), previousBlock.getPreviousHash());
            validateHelper(previousBlock.getPreviousBlock());
        }
    }

    private void validateHash(Block block, String hash) throws LedgerException {
        if (!hash.equals(
                makeHash(block.getBlockNumber(), block.getPreviousHash(),
                        block.getPreviousBlock(), block.getTransactionList(),
                        block.getAccountBalanceMap()))) {
            throw new LedgerException("validate", "re-computed hash of block does not match hash");
        }
    }

    private String makeHash(int blockNumber, String previousHash, Block previousBlock,
                            ArrayList<Transaction> transactions, HashMap<String, Account> accountBalanceMap)
            throws LedgerException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String text = name + seed + description + blockNumber + previousHash + previousBlock + transactions + accountBalanceMap;
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new LedgerException("process-transaction", "unable to create new block");
        }
    }
}
