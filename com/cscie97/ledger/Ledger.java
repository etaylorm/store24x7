package com.cscie97.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.security.NoSuchAlgorithmException;

/**
 * The service that manages the blockchain
 * The ledger is responsible for validating and processing
 * transactions, creating Accounts, creating Blocks,
 * and validating the state of the blockchain
 */
class Ledger {
    private String name;
    private String description;
    private String seed;
    private HashMap<Integer, Block> blockMap;
    private Block genesisBlock;
    private Block currentBlock;

    Ledger(String name, String description, String seed) {
        // provided details of the ledger
        this.name = name;
        this.description = description;
        this.seed = seed;

        blockMap = new HashMap<>(); // map of blockNumbers to blocks

        // initialized genesis block
        genesisBlock = new Block(1, null, new Block(), new HashMap<>(), new HashMap<>());
        blockMap.put(genesisBlock.getBlockNumber(), genesisBlock);
        currentBlock = genesisBlock; // set current block reference to the genesis block

        // initialize the master account
        Account master = new Account("master", Integer.MAX_VALUE);
        // add the master account to the map of accounts wit pending balances
        currentBlock.addAccountPendingAccountBalanceMap(master);
    }

    /**
     * Getter for name of ledger
     * @return name
     */
    String getName(){
        return name;
    }

    /**
     * Creates a new account and adds the account to the current blocks
     * pending account balance map
     * @param accountId unique identifier for the account provided to the ledger
     * @return accountId for successfully created account
     * @throws LedgerException if the account already exists
     */
    String createAccount(String accountId) throws LedgerException {
        if (currentBlock.getPendingAccountBalanceMap().get(accountId) == null) {
            Account account = new Account(accountId);
            currentBlock.addAccountPendingAccountBalanceMap(account);
            return accountId;
        } else {
            throw new LedgerException("create-account", "account already exists");
        }
    }

    /**
     * Processes a transaction submitted to the ledger
     *
     * The transaction must pass the following criteria:
     * 1. The transaction ID must be a unique identified (i.e. no previous transactions
     * can have the same ID)
     * 2. The payer and receiver accounts within the transaction must exist
     * 3. The payer account must have sufficient balance for the amount and fee
     * 4. The fee must be at least 10
     *
     * If the transaction is valid, it is added to the current block's list of
     * transactions and the pending balances of the payer, receiver, and master
     * acocounts are updated
     * @param transaction new transaction to be processed
     * @return transactionId for successfully processed transaction
     * @throws LedgerException if transaction is not valid
     */
    String processTransaction(Transaction transaction) throws LedgerException {
        // validate transaction first
        String payerAddress = transaction.getPayer().getAddress();
        String receiverAddress = transaction.getReceiver().getAddress();

        // test whether the transactionID is a duplicate by attempting to get a transaction with the ID
        Transaction duplicateTransactionId = getTransaction(transaction.getTransactionId());

        if (duplicateTransactionId != null){
            throw new LedgerException("process-transaction", "transaction ID already exists");
        }

        // check that both accounts exist using their addresses
        if (!currentBlock.getPendingAccountBalanceMap().containsKey(payerAddress)
                | !currentBlock.getPendingAccountBalanceMap().containsKey(payerAddress)) {
            throw new LedgerException("process-transaction", "account does not exist");
        }

        // if both accounts exist, retrieve references to the accounts to be updated
        Account payer = currentBlock.getPendingAccountBalanceMap().get(payerAddress);
        Account receiver = currentBlock.getPendingAccountBalanceMap().get(receiverAddress);
        Account master = currentBlock.getPendingAccountBalanceMap().get("master");

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

        // add the validated transaction to the current block
        currentBlock.addTransaction(transaction);

        // if the current block now has 10 transactions, close the block and add a new block
        // to the chain
        if (currentBlock.getTransactionList().size() == 10){
            addBlockToChain();
        }

        return transaction.getTransactionId();
    }

    /**
     * Finalizes the current block, generates a new block with a
     * reference to the finalized block
     * @throws LedgerException if block hash cannot be calculated or is invalid
     */
    private void addBlockToChain() throws LedgerException {
        // update the account balance map with the uncommitted balances stored in the pending
        // account balance map
        currentBlock.setAccountBalanceMap(currentBlock.getPendingAccountBalanceMap());

        // generate block's hash based on all finalized properties and associations
        currentBlock.setHash(makeHash(currentBlock));

        // validate finalized block
        currentBlock.validate();

        // create a copy of the finalized block's account balance map to pass to the new block
        HashMap<String, Account> accountBalancesMap = copyAccountBalanceMap(currentBlock.getAccountBalanceMap());
        HashMap<String, Account> pendingAccountBalanceMap = copyAccountBalanceMap(currentBlock.getAccountBalanceMap());

        // create a new block
        int newBlockNumber = currentBlock.getBlockNumber() + 1;
        Block newBlock = new Block(newBlockNumber, currentBlock.getHash(), currentBlock, accountBalancesMap, pendingAccountBalanceMap);

        // add new block to the ledger's block map
        blockMap.put(newBlockNumber, newBlock);
        currentBlock = newBlock; // update reference
    }

    /**
     * Create a full copy of an account map
     * Used when creating a new block to ensure account balance map
     * of new block does not refer to previous block's map
     * @param map map of accounts by their addresses
     * @return copy of the map
     */
    private HashMap<String, Account> copyAccountBalanceMap(HashMap<String, Account> map) {
        HashMap<String, Account> copyOfAccountBalanceMap = new HashMap<>();

        for (String accountId : map.keySet()) {
            copyOfAccountBalanceMap.put(accountId, new Account(accountId, map.get(accountId).getBalance()));
        }
        return copyOfAccountBalanceMap;
    }

    /**
     * Get the committed account balance of a given account
     * @param address unique identifier of an account
     * @return committed balance
     * @throws LedgerException if account doesn't exist or account has no committed balance
     */
    int getAccountBalance(String address) throws LedgerException {
        try {
            return currentBlock.getAccountBalanceMap().get(address).getBalance();
        }
        catch (NullPointerException e) {
            throw new LedgerException("get-account-balance", "account has no committed balance");
        }
    }

    /**
     * Get map of account IDs to their committed balances
     * @return map of accounts to balances
     */
    HashMap<String, Integer> getAccountBalances() {
        HashMap<String, Integer> balanceMap = new HashMap<>();
        HashMap<String, Account> accountBalanceMap = currentBlock.getAccountBalanceMap();

        for (String accountId : accountBalanceMap.keySet()) {
            balanceMap.put(accountId, accountBalanceMap.get(accountId).getBalance());
        }
        return balanceMap;
    }

    /**
     * Returns a block by unique identifier
     * @param blockNumber unique identifier of a block
     * @return Block
     * @throws LedgerException if block doesn't exist
     */
    Block getBlock(int blockNumber) throws LedgerException {
        if (blockMap.get(blockNumber) == null) {
            throw new LedgerException("get-block", "block " + blockNumber + " not exist");
        } else {
            return blockMap.get(blockNumber);
        }
    }

    /**
     * Get transaction by unique identifier
     * @param transactionId unique identifier
     * @return Transaction
     */
    Transaction getTransaction(String transactionId) {
        // calls recursive helper method to search through all previous blocks in addition
        // to current block
        return getTransactionHelper(transactionId, currentBlock);
    }

    /**
     * Recursive helper method to search previous blocks
     * @param transactionId unique identifier for a transaction
     * @param thisBlock block to look for the transaction within
     * @return transaction with target transactionId
     */
    private Transaction getTransactionHelper(String transactionId, Block thisBlock){
        for (Transaction transaction : thisBlock.getTransactionList()) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        // if we've just searched the genesis block, stop searching and return null
        if (thisBlock.equals(genesisBlock)) {
            return null;
        } else {
            return getTransactionHelper(transactionId, thisBlock.getPreviousBlock());
        }
    }

    /**
     * Validates the entire blockchain by validating each block
     * using the block's validate method and validating the connection
     * between the blocks by recomputing the hash of a block and verifying
     * it matches the saved hash
     * @throws LedgerException
     */
    void validate() throws LedgerException {
        validateHelper(currentBlock); // calls recursive helper
    }

    /**
     * Recursive helper method that validates each block in the blockchain
     * @param block current block
     * @throws LedgerException if the blockchain is not valid
     */
    private void validateHelper(Block block) throws LedgerException {
        // if we haven't reached the genesis block, validate the previous block
        if (!block.equals(genesisBlock)) {
            validateHash(block.getPreviousBlock(), block.getPreviousHash());
            Block thisBlock = block.getPreviousBlock();
            thisBlock.validate();
            validateHelper(thisBlock);
        }
    }

    /**
     * Validate that the hash can be re-computed using the properties and associations
     * of the block
     * @param block block to recompute the hash of
     * @param hash hash to compare against
     * @throws LedgerException if the hashes do not match
     */
    private void validateHash(Block block, String hash) throws LedgerException {
        if (!hash.equals(makeHash(block))) {
            throw new LedgerException("validate", "re-computed hash of block does not match hash");
        }
    }

    /**
     * Computes the hash for a block based on its properties and associations in addition
     * to the characteristics of the ledger
     * @param block to be hashed
     * @return String hash
     * @throws LedgerException if hashing fails
     */
    private String makeHash(Block block)
            throws LedgerException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String text = name + seed + description + block.getBlockNumber() + block.getPreviousHash()
                    + block.getPreviousBlock() + block.getTransactionList() + block.getAccountBalanceMap();

            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new LedgerException("process-transaction", "unable to create new block");
        }
    }
}
