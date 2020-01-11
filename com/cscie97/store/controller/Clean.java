package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

/**
 * Clean command to tidy the aisles or
 * to remove any products on the floors of the aisles or in inappropriate inventories
 */
public class Clean implements Command {
	StoreModelServiceInterface storeModel;
	String storeId;
	String aisleId;
	String productId;
	String auth_token;

	Clean(StoreModelServiceInterface storeModel, String storeId, String aisleId, String productId, String auth_token){
		this.storeModel = storeModel;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.productId = productId;
		this.auth_token = auth_token;
	}

	Clean(StoreModelServiceInterface storeModel, String storeId, String aisleId, String auth_token){
		this.storeModel = storeModel;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.auth_token = auth_token;
	}

	/**
	 * Execute issues command to the robot to clean up the aisle and then update the robot's location
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		String robotId = storeModel.findNearestDevice(storeId, aisleId, "robot", auth_token);
		storeModel.createCommand(
				storeId,
				robotId,
				"clean " + aisleId,
				auth_token);
		storeModel.updateRobotLocation(robotId, storeId, aisleId, auth_token);
	}
}
