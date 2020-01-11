package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.model.StoreModelServiceInterface;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Command generating class that takes an event as input and determines what type of command to return
 * Also takes a the store model service, ledger, storeid, and device Id as necessary context to pass to
 * the appropriate command's constructor
 */
class CommandFactory {
    /**
     * Returns a command of the appropriate type based on the event input
     * @param storeModel to use in creating the command class
     * @param ledger to use in creating the command class
     * @param authService authentication service to check permissions for commands
     * @param storeId context for the command
     * @param deviceId context for the command
     * @param event details of the event
     * @return command interface
     */
    Command getCommand(StoreModelServiceInterface storeModel,
                       LedgerInterface ledger,
                       AuthenticationServiceInterface authService,
                       String storeId,
                       String deviceId,
                       String event,
                       String auth_token) {
        Command command;

        ArrayList<String> eventDetails = new ArrayList<>();
        String[] words = event.split(" ");

        eventDetails.addAll(Arrays.asList(words));
        String eventType = eventDetails.get(0);

        switch(eventType){
            case("clean"):
                // format: clean <product> <aisle>
                command = new Clean(storeModel, storeId, eventDetails.get(2), eventDetails.get(1), auth_token);
                break;
            case("broken"):
                // format: broken glass in aisle <aisle>
                command = new Clean(storeModel, storeId, eventDetails.get(4), auth_token);
                break;
            case("checkout"):
                // format: checkout customer <customer> with account <account> print --<type:value>--
                command = new Checkout(storeModel, ledger, authService, storeId, deviceId, eventDetails.get(2), eventDetails.get(7), eventDetails.get(5), auth_token);
                break;
            case("emergency"):
                // format: emergency <emergency> in <aisle>
                command = new Emergency(storeModel, storeId, eventDetails.get(3), eventDetails.get(1), auth_token);
                break;
            case("location"):
                // format: location customer <customer> <aisle>
                command = new CustomerSeen(storeId, eventDetails.get(2), eventDetails.get(3), storeModel, auth_token);
                break;
            case("enter"):
                // format: enter customer <customer> with account <account> print --<type:value>--
                command = new EnterStore(storeId, eventDetails.get(2), eventDetails.get(7), deviceId, eventDetails.get(5), auth_token, storeModel, ledger, authService);
                break;
            case("find"):
                // format: find <customer>
                command = new FindCustomer(storeId, deviceId, eventDetails.get(1), storeModel, auth_token);
                break;
            case("product"):
                // format: product <number> <product> for customer <customer> print --<type:value>--
                command = new FetchProduct(storeModel,
                        authService,
                        storeId,
                        deviceId,
                        eventDetails.get(5),
                        eventDetails.get(7),
                        eventDetails.get(2),
                        Integer.parseInt(eventDetails.get(1)),
                        auth_token);
                break;
            case("tally"):
                // format: tally customer <customer> account <account> print --<type:value>--
                command = new CheckAccountBalance(
                        storeModel,
                        ledger,
                        authService,
                        storeId,
                        deviceId,
                        eventDetails.get(2),
                        eventDetails.get(6),
                        eventDetails.get(4),
                        auth_token);
                break;
            case("basket"):
                // format: basket <customer> (adds|removes) <count> <product> from <aisle> <shelf>.
                int amount = Integer.parseInt(eventDetails.get(3));
                if (eventDetails.get(2).equals("adds")){
                    amount = amount * -1;
                }
                command = new BasketEvent(
                        storeModel,
                        storeId,
                        eventDetails.get(6),
                        eventDetails.get(7),
                        eventDetails.get(1),
                        eventDetails.get(4),
                        amount,
                        auth_token);
                break;
            default:
                command = null;
        }
        return command;
    }
}