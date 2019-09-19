package com.cscie97.ledger.test;
import com.cscie97.ledger.CommandProcessor;
import com.cscie97.ledger.CommandProcessorException;
import java.io.File;

/**
 * Test driver for the blockchain ledger
 * Accepts command file to be processed by the command processor
 */
public class TestDriver {
    public static void main(String[] args) throws CommandProcessorException {
        // get file name from command line
        String test_file_name = args[0];
        // convert filename to File object and pass along to the command processor
        CommandProcessor.processCommandFile(new File(test_file_name));
    }
}
