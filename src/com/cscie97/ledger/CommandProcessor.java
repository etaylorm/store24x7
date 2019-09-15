package com.cscie97.ledger;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {
    // should you be able to make multiple ledgers?
    private static Ledger ledger;

    public static void processCommandFile(File file_path) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path))) {
            String command = bufferedReader.readLine();
            int lineNumber = 0;
            while (command != null) {
                lineNumber ++;
                if (!command.startsWith("#") && command.length() > 0) {
                    try {
                        processCommand(command);
                    } catch (CommandProcessorException c) {
                        System.out.println(c);
                    }
                }
                command = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR]: " + e);
        }
    }

    // process-transaction 1 amount 1000 fee 10 payload "fund account" payer master receiver mary
    private static void processCommand(String command) throws CommandProcessorException {
        ArrayList<String> args = new ArrayList<>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            args.add(regexMatcher.group().toLowerCase());
        }
        String action = args.get(0);
        System.out.println(command);
        try {
            switch (action) {
                case ("create-ledger"):
                    ledger = new Ledger(args.get(1), args.get(3), args.get(5));
                    break;
                case ("create-account"):
                    String accountId = ledger.createAccount(args.get(1));
                    System.out.println("Account with ID " + accountId + " created");
                    break;
                case ("process-transaction"):
                    String transactionId = ledger.processTransaction(
                                                new Transaction(args.get(1),
                                                        Integer.parseInt(args.get(3)),
                                                        Integer.parseInt(args.get(5)),
                                                        args.get(7),
                                                        new Account(args.get(9)),
                                                        new Account(args.get(11))));
                    System.out.println("Transaction with id " + transactionId + " committed");
                    break;
                case ("get-account-balance"):
                    System.out.println(ledger.getAccountBalance(args.get(1)));
                    break;
                case ("get-account-balances"):
                    System.out.println(ledger.getAccountBalances());
                    break;
                case ("get-block"):
                    ledger.getBlock(Integer.parseInt(args.get(1)));
                    System.out.println(args.get(1));
                    break;
                case ("get-transaction"):
                    System.out.println(ledger.getTransaction(args.get(1)));
                    break;
                case ("validate"):
                    ledger.validate();
                    break;
                default:
                    // this needs to be fixed
                    throw new CommandProcessorException(action, "action not found");
            }
        } catch (LedgerException l) {
            System.out.println(l);
        }
    }
}
