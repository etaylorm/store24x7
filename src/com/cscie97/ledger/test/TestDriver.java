package com.cscie97.ledger.test;
import com.cscie97.ledger.CommandProcessor;
import java.io.File;
import java.security.NoSuchAlgorithmException;

public class TestDriver {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // get file name from command line
        String test_file_name = args[0];
        // convert filename to File object and pass along to the command processor
        CommandProcessor.processCommandFile(new File(test_file_name));
    }
}
