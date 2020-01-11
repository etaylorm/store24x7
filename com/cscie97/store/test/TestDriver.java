package com.cscie97.store.test;
import com.cscie97.ledger.Ledger;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AuthenticationService;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.controller.StoreController;
import com.cscie97.store.controller.StoreControllerInterface;
import com.cscie97.store.model.*;

import java.io.File;

/**
 * Test driver for the Store 24x7 system
 */
public class TestDriver {
    public static void main(String[] args) throws CommandProcessorException {
        // create the store model interface
        StoreModelServiceInterface storeModelServiceInterface = StoreModelService.getInstance();
        // create the ledger interface
        LedgerInterface ledgerInterface = new Ledger("test", "ledger for controller", "seed123");

        // create the authentication service interface
        AuthenticationServiceInterface authenticationServiceInterface = AuthenticationService.getInstance();

        // create the store controller and pass references to the store model and the ledger
        StoreControllerInterface storeControllerInterface = StoreController.getInstance();
        storeControllerInterface.addStoreModelService(storeModelServiceInterface);
        storeControllerInterface.addLedger(ledgerInterface);
        storeControllerInterface.addAuthService(authenticationServiceInterface);

        // register the store controller as an observer of the store model service
        storeModelServiceInterface.register(storeControllerInterface);
        // add the auth service to the store model service
        storeModelServiceInterface.addAuthService(authenticationServiceInterface);

        // get file name from command line
        String test_file_name = args[0];
        // convert filename to File object and pass along to the command processor
        CommandProcessor.processCommandFile(new File(test_file_name), storeModelServiceInterface, ledgerInterface, authenticationServiceInterface);
    }
}

