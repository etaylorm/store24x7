package com.cscie97.ledger;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.security.NoSuchAlgorithmException;

public class CommandProcessor {
    public static Ledger ledger;

    public static void processCommandFile(File file_path) throws NoSuchAlgorithmException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_path))) {
            String command = bufferedReader.readLine();
            while (command != null) {
                if (!command.startsWith("#") && command.length() > 0) {
                    processCommand(command);
                }
                command = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR]: " + e);
        }
    }

    // process-transaction 1 amount 1000 fee 10 payload "fund account" payer master receiver mary
    private static void processCommand(String command) throws NoSuchAlgorithmException {
        String[] args = command.split(" ");
        //System.out.println(command);
        switch (args[0].toLowerCase()) {
            case ("create-ledger"):
                System.out.println(command);
                ledger = new Ledger(args[1], args[3], args[5]);
                System.out.println(ledger.getAccountBalance("master"));
                break;
            case ("create-account"):
                System.out.println(command);
                System.out.println(ledger.createAccount(args[1]));
                break;
            case ("process-transaction"):
                System.out.println(command);
                Account payer = new Account(args[Arrays.asList(args).indexOf("payer") + 1]);
                Account receiver = new Account(args[Arrays.asList(args).indexOf("receiver") + 1]);
                ledger.processTransaction(new Transaction(args[1], Integer.parseInt(args[3]), Integer.parseInt(args[5]), args[7], payer, receiver));
                break;
            case ("get-account-balance"):
                System.out.println(command);
                System.out.println(ledger.getAccountBalance(args[1]));
                break;
            case ("get-account-balances"):
                break;
            case ("get-block"):
                //  System.out.println("get-block");
                break;
            case ("get-transaction"):
                System.out.println(command);
                break;
            case ("validate"):
                //  System.out.println("validate");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + args[0].toLowerCase());
        }
    }
}
