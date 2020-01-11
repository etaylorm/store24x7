package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command to add or remove item(s) from a customer's basket. Implements the command interface.
 */
public class BasketEvent implements Command {
	StoreModelServiceInterface storeModel;
	String storeId;
	String aisleId;
	String shelfId;
	String customerId;
	String productId;
	int count;
	String auth_token;

	BasketEvent(StoreModelServiceInterface storeModel,
				String storeId,
				String aisleID,
				String shelfId,
				String customerId,
				String productId,
				int count,
				String auth_token) {
		this.storeModel = storeModel;
		this.storeId = storeId;
		this.aisleId = aisleID;
		this.shelfId = shelfId;
		this.customerId = customerId;
		this.productId = productId;
		this.count = count;
		this.auth_token = auth_token;
	}

	/**
	 * Execute method for the command
	 * 1. Updates the customer's basket
	 * 2. Updates the inventory that the customer affected
	 * 3. Attempt to restock the inventory that the customer affected
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		// update the customer's basket
		storeModel.updateCustomerBasket(customerId, productId, count, auth_token);

		// show basket items
		storeModel.showBasketItems(customerId, auth_token);

		// get the id of the inventory the customer affected
		String inventoryId = storeModel.getInventory(storeId, aisleId, shelfId, productId, auth_token);

		// update the relevant inventory
		storeModel.updateInventory(storeId, inventoryId, -1 * count, auth_token);

		// show inventory
		storeModel.showInventory(storeId, inventoryId, auth_token);

		// if customer removed an item, issue command to robot to restock the shelf
		if (count > 0) {
			storeModel.restock(storeId, inventoryId, count, auth_token);
			storeModel.createCommand(
					storeId,
					storeModel.findNearestDevice(storeId, aisleId, "robot", auth_token),
					"restock inventory " + inventoryId,
					auth_token);
		}
	}
}
