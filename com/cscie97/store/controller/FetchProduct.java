package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command to fetch a product for a customer. Created in response to a customer requesting a product.
 * Implements command interface.
 */
public class FetchProduct implements Command {
	StoreModelServiceInterface storeModel;
	AuthenticationServiceInterface authService;
	String storeId;
	String aisleId;
	String customerId;
	String userPrint;
	String productId;
	int count;
	String auth_token;

	FetchProduct(StoreModelServiceInterface storeModel, AuthenticationServiceInterface authService, String storeId, String aisleId, String customerId, String userPrint, String productId, int count, String auth_token){
		this.storeModel = storeModel;
		this.authService = authService;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.customerId = customerId;
		this.userPrint = userPrint;
		this.productId = productId;
		this.count = count;
		this.auth_token = auth_token;
	}

	/**
	 * Execute the fetch product command
	 * 0. Check that the user has permission to command the robot
	 * 1. Locate the product within the store
	 * 2. Command the robot to fetch the product
	 * 3. Update inventory that the robot affected
	 * 4. Attempt to restock inventory
	 * 5. Add product to the customer's basket
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		// check that the user has permission to command the robot
		String customerAuthToken = authService.authenticateUserPrint(customerId, userPrint);
		authService.checkPermission(customerAuthToken, "commandrobot", storeId);

		// find the product in the store
		String inventoryId = storeModel.findProduct(storeId, productId, count, auth_token);

		// issue command to robot to fetch the product for the customer
		storeModel.createCommand(storeId,
				storeModel.findNearestDevice(storeId, aisleId, "robot", auth_token),
				"fetch " + count + " from inventory " + inventoryId + " for customer " + customerId,
				auth_token);

		// update customer basket
		storeModel.updateCustomerBasket(customerId, productId, count, auth_token);

		// show the basket
		storeModel.showBasketItems(customerId, auth_token);

		// update the inventory the robot affected and restock it if possible
		storeModel.updateInventory(storeId, inventoryId, -1 * count, auth_token);

		// show the inventory
		storeModel.showInventory(storeId, inventoryId, auth_token);

		// restock if possible
		storeModel.restock(storeId, inventoryId, count, auth_token);
		storeModel.createCommand(
				storeId,
				storeModel.findNearestDevice(storeId, aisleId, "robot", auth_token),
				"restock inventory " + inventoryId,
				auth_token);
	}
}
