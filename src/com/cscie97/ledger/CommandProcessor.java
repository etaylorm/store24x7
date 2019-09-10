package com.cscie97.ledger;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CommandProcessor {
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

    private static void processCommand(String command) throws NoSuchAlgorithmException {
        String[] args = command.split(" ");
        switch (args[0].toLowerCase()){
            case ("create-ledger"):
                System.out.println("create-ledger");
                Ledger ledger = new Ledger(args[1], args[3], args[5]);
                System.out.println(ledger.getAccountBalance("master"));
            case ("create-account"):
                System.out.println("create-account");
            case("process-transaction"):
                System.out.println("process-transaction");
            case("get-account-balance"):
                System.out.println("get-account-balance");
            case("get-account-balances"):
                System.out.println("get-account-balances");
            case("get-block"):
                System.out.println("get-block");
            case("get-transaction"):
                System.out.println("get-transaction");
            case("validate"):
                System.out.println("validate");
        }
        System.out.println(command);
    }
}
