package com.cscie97.store.controller;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

import java.util.ArrayList;

/**
 * Command issued in response to an emergency within the store
 * Implements the command interface
 */
public class Emergency implements Command {
	StoreModelServiceInterface storeModel;
	String storeId;
	String aisleId;
	String emergency;
	String auth_token;

	Emergency(StoreModelServiceInterface storeModel, String storeId, String aisleId, String emergency, String auth_token){
		this.storeModel = storeModel;
		this.storeId = storeId;
		this.aisleId = aisleId;
		this.emergency = emergency;
		this.auth_token = auth_token;
	}

	/**
	 * Execute method for the command
	 * 1. Open all turnstiles within the store with the emergency
	 * 2. Announce the emergency on all the speakers
	 * 3. Send all robots but the nearest robot to the emergency to assist customers
	 * 4. Send the nearest robot to deal with the emergency
	 * @throws StoreModelServiceException
	 */
	public void execute() throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		// open turnstiles first
		ArrayList<String> turnstiles = storeModel.getDevices(storeId, "turnstile", auth_token);

		for (String turnstileId: turnstiles){
			storeModel.createCommand(storeId, turnstileId, "open", auth_token);
		}

		// announce emergency
		ArrayList<String> speakers = storeModel.getDevices(storeId, "speaker", auth_token);

		for (String speakerId: speakers){
			storeModel.createCommand(storeId,
					speakerId,
					"speak there is a " + emergency + " in " + aisleId + " please leave " + storeId + " immediately",
					auth_token);
		}

		// send robots to address emergency and help customers
		ArrayList<String> robots = storeModel.getDevices(storeId, "robot", auth_token);
		String nearestRobotId = storeModel.findNearestDevice(storeId, aisleId, "robot", auth_token);

		for (String robotId: robots){
			if (robotId.equals(nearestRobotId)){
				storeModel.createCommand(storeId, robotId, "address " + emergency + " in " + aisleId, auth_token);
				storeModel.updateRobotLocation(robotId, storeId, aisleId, auth_token);
			} else {
				storeModel.createCommand(storeId, robotId, "assist customers leaving " + storeId, auth_token);
			}
		}
	}
}
