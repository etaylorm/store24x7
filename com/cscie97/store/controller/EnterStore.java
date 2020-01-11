package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command invokes when a customer attempts to enter the store
 */
public class EnterStore implements Command {
	private StoreModelServiceInterface storeModel;
	private LedgerInterface ledger;
	private AuthenticationServiceInterface authService;
	private String storeId;
	private String deviceId;
	private String customerId;
	private String userPrint;
	private String accountId;
	private String auth_token;

	EnterStore(String storeId, String customerId, String userPrint, String deviceId, String accountId, String auth_token, StoreModelServiceInterface storeModel, LedgerInterface ledger, AuthenticationServiceInterface authService){
		this.storeId = storeId;
		this.customerId = customerId;
		this.userPrint = userPrint;
		this.deviceId = deviceId;
		this.accountId = accountId;
		this.auth_token = auth_token;
		this.storeModel = storeModel;
		this.ledger = ledger;
		this.authService = authService;
	}

	/**
	 * Execute the enter store command
	 * 0. Check that the customer has permission to enter the store
	 * 1. Check that the customer has a registered ledger account with positive balance
	 * 2. Ensure that the customer is known to the store 24x7 system
	 * 3. Assign the customer a basket
	 * 4. Open the turnstile
	 * 5. Close the turnstile after th customer
	 * @throws StoreModelServiceException
	 * @throws LedgerException
	 */
	public void execute() throws StoreModelServiceException, LedgerException, ObserverException, AccessDeniedException, InvalidAuthTokenException {
		// check that the customer has permission to enter the store
		String customerAuthToken = authService.authenticateUserPrint(customerId, userPrint);
		authService.checkPermission(customerAuthToken, "enterstore", storeId);

		// check that the customer has an account and the balance is positive
		if (ledger.getAccountBalance(accountId) <= 0){
			throw new ObserverException("enter store", "customer has 0 balance");
		}

		// assign the customer a basket
		storeModel.getCustomerBasket(customerId, auth_token);

		// show customer details
		storeModel.showCustomer(customerId, auth_token);

		// open and close the turnstile
		storeModel.createCommand(storeId, deviceId, "open", auth_token);
		storeModel.createCommand(storeId, deviceId, "close", auth_token);
	}
}
