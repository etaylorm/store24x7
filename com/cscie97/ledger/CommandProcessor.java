package com.cscie97.ledger;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CommandProcessor processes a file
 * of commands or an individual command string
 * and passes the parsed commands to the Ledger
 */
public class CommandProcessor {
    private static Ledger ledger;

    /**
     * Processes commands from an input file
     * @param file_path the address of the file containing
     *                  commands
     * @throws CommandProcessorException
     */
    public static void processCommandFile(File file_path) throws CommandProcessorException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path))) {
            String command = bufferedReader.readLine();
            int lineNumber = 0;
            while (command != null) {
                lineNumber ++;
                System.out.println(command);
                if (!command.startsWith("#") && command.length() > 0) {
                    try {
                        processCommand(command);
                    } catch (CommandProcessorException e) {
                        System.out.println(e);
                        throw new CommandProcessorException(e.getCommand(), e.getReason(), lineNumber);
                    }
                }
                command = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new CommandProcessorException("process from file", "IO exception");
        }
    }

    /**
     * Parses a single command and performs the appropriate action
     * @param command single command to be parsed
     * @throws CommandProcessorException
     */
    private static void processCommand(String command) throws CommandProcessorException {
        ArrayList<String> args = new ArrayList<>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(command);

        while (regexMatcher.find()) {
            args.add(regexMatcher.group().toLowerCase());
        }

        String action = args.get(0);

        try {
            switch (action) {
                case ("create-ledger"): // create a new ledger (assumed only one will be created)
                    ledger = new Ledger(args.get(1), args.get(3), args.get(5));
                    System.out.println("ledger " + ledger.getName() + " created");
                    break;
                case ("create-account"):
                    String accountId = ledger.createAccount(args.get(1));
                    System.out.println("account with ID " + accountId + " created");
                    break;
                case ("process-transaction"): // creates and submits new transaction based on inputs
                    String transactionId = (
                            ledger.processTransaction(
                                    new Transaction(args.get(1),
                                                    Integer.parseInt(args.get(3)),
                                                    Integer.parseInt(args.get(5)),
                                                    args.get(7),
                                                    new Account(args.get(9)),
                                                    new Account(args.get(11)))));
                    System.out.println("transaction with ID: " + transactionId + " created");
                    break;
                case ("get-account-balance"): // prints committed account balance for a given Account by address
                    System.out.println(ledger.getAccountBalance(args.get(1)));
                    break;
                case ("get-account-balances"): // returns committed account balances for all accounts
                    System.out.println(ledger.getAccountBalances());
                    break;
                case ("get-block"): // prints out details of a block by block number
                    System.out.println(ledger.getBlock(Integer.parseInt(args.get(1))).getInfoString());
                    break;
                case ("get-transaction"): // prints out details for a given transaction by transaction ID
                    System.out.println(ledger.getTransaction(args.get(1)));
                    break;
                case ("validate"): // validates the integrity of the entire blockchain
                    ledger.validate();
                    System.out.println("blockchain validated");
                    break;
                default:
                    // if passed command does not match valid commands above, raise exception
                    throw new CommandProcessorException(action, "supplied command not found");
            }
        } catch (LedgerException e) {
            System.out.println(e);
        }
    }
}
