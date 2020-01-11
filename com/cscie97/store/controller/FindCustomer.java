package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command to locate a given customer.
 * Implements command interface.
 */
public class FindCustomer implements Command {
	private String customerId;
	private String storeId;
	private String aisleId;
	private StoreModelServiceInterface storeModel;
	private String auth_token;

	FindCustomer(String storeId, String aisleId, String customerId, StoreModelServiceInterface storeModel, String auth_token){
		this.customerId = customerId;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.storeModel = storeModel;
		this.auth_token = auth_token;
	}

	/**
	 * Execute the find customer command
	 * 1. Get the customer's location from the store model API
	 * 2. Issue a command to the nearest speaker to the device that registered the request
	 * to announce the customer's location
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		String location = storeModel.findCustomerLocation(customerId, auth_token);
		storeModel.createCommand(storeId,
				storeModel.findNearestDevice(storeId, aisleId, "speaker", auth_token),
				"announce " + customerId + " is in " + location, auth_token);
	}
}
