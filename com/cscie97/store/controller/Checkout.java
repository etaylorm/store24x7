package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

import java.time.LocalTime;

/**
 * Checkout command used when customers approach the turnstiles and attempt to check out of the 24x7 store
 * Implements the command interface
 */
public class Checkout implements Command {
	StoreModelServiceInterface storeModel;
	LedgerInterface ledger;
	AuthenticationServiceInterface authService;
	String storeId;
	String turnstileId;
	String customerId;
	String customerPrint;
	String accountId;
	String auth_token;

	Checkout(StoreModelServiceInterface storeModel, LedgerInterface ledger, AuthenticationServiceInterface authService, String storeId, String turnstileId,
			 String customerId, String customerPrint, String accountId, String auth_token){
		this.storeModel = storeModel;
		this.ledger = ledger;
		this.authService = authService;
		this.storeId = storeId;
		this.turnstileId = turnstileId;
		this.customerId = customerId;
		this.customerPrint = customerPrint;
		this.accountId = accountId;
		this.auth_token = auth_token;
	}

	/**
	 * Execute command for the checkout command
	 * 0. Check that the customer is permitted to check out
	 * 1. Tallies the customer's basket total
	 * 2. Adds the transaction to the ledger (may raise exception if the customer does not have a sufficient balance)
	 * 3. Clears the customer's basket
	 * 4. Issues a command to open the turnstile
	 * 5. Issues a command to says goodbye to the customer
	 * 6. Issues a command to close the turnstile
	 * 7. Checks whether the customer's basket items are over 10lbs. If so, issues a command to a robot
	 * to assist them to their car
	 * @throws StoreModelServiceException
	 * @throws LedgerException
	 */
	public void execute() throws StoreModelServiceException, LedgerException, AccessDeniedException, InvalidAuthTokenException {
		// check that the customer has permission to check out
		String customerAuthToken = authService.authenticateUserPrint(customerId, customerPrint);
		authService.checkPermission(customerAuthToken, "checkout", storeId);

		// tally customer basket total
		int total = storeModel.tallyCustomerBasket(customerId, auth_token);

		// process transaction and clear basket
		ledger.addTransaction(accountId + auth_token + LocalTime.now(), total, 10, "checkout", accountId, storeId);
		storeModel.clearBasket(customerId, auth_token);

		// open the turnstile
		storeModel.createCommand(storeId, turnstileId, "open", auth_token);

		// say goodbye to the customer
		storeModel.createCommand(storeId, turnstileId, "speak goodbye " + customerId, auth_token);

		// close the turnstile
		storeModel.createCommand(storeId, turnstileId, "close", auth_token);

		float weight = storeModel.weighCustomerBasket(customerId, auth_token);
		if (weight > 0){
			storeModel.createCommand(storeId,
					storeModel.findDevice(storeId, "robot", auth_token),
					"assist customer to car",
					auth_token);
		}
	}
}
