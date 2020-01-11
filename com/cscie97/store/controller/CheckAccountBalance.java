package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command raised by a customer asking for their current account balance and the tally of items
 * within their baset. Implements command interface.
 */
public class CheckAccountBalance implements Command {
	StoreModelServiceInterface storeModel;
	LedgerInterface ledger;
	AuthenticationServiceInterface authService;
	String storeId;
	String aisleId;
	String customerId;
	String userPrint;
	String accountId;
	String auth_token;

	CheckAccountBalance(StoreModelServiceInterface storeModel, LedgerInterface ledger, AuthenticationServiceInterface authService, String storeId, String aisleId, String customerId, String userPrint, String accountId, String auth_token){
		this.storeModel	= storeModel;
		this.ledger = ledger;
		this.authService = authService;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.customerId = customerId;
		this.userPrint = userPrint;
		this.accountId = accountId;
		this.auth_token = auth_token;
	}

	/**
	 * Executes the check account balance command
	 * 0. Check that the customer is permitted to command the robot
	 * 1. Gets the customer's balance from the ledger
	 * 2. Tallies the customer's basket
	 * 3. Issues a command to the nearest speaker to tell the customer their balance and total
	 * @throws StoreModelServiceException
	 * @throws LedgerException
	 */
	public void execute() throws StoreModelServiceException, LedgerException, AccessDeniedException, InvalidAuthTokenException {
		// check that the customer has permission to command the robot
		String customerAuthToken = authService.authenticateUserPrint(customerId, userPrint);
		authService.checkPermission(customerAuthToken, "commandRobot", storeId);

		int accountBalance = ledger.getAccountBalance(accountId);
		int basketBalance = storeModel.tallyCustomerBasket(customerId, auth_token);

		storeModel.createCommand(storeId,
				storeModel.findNearestDevice(storeId, aisleId, "speaker", auth_token),
				"speak total value of basket items is " + basketBalance + " your account balance is " + accountBalance,
				auth_token);
	}
}
