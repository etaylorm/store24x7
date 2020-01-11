package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Command to update a customer's location when a camera detects a change in their location
 * Implements command interface
 */
public class CustomerSeen implements Command {
	private StoreModelServiceInterface storeModel;
	private String customerId;
	private String storeId;
	private String aisleId;
	private String auth_token;

	CustomerSeen(String storeId, String customerId, String aisleId, StoreModelServiceInterface storeModel, String auth_token){
		this.storeModel = storeModel;
		this.storeId = storeId;
		this.customerId = customerId;
		this.aisleId = aisleId;
		this.auth_token = auth_token;
	}

	/**
	 * Calls the store model method updateCustomer with the new location
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		storeModel.updateCustomer(customerId, storeId, aisleId, auth_token);
		storeModel.showCustomer(customerId, auth_token);
	}
}
