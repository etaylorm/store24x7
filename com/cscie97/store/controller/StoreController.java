package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerException;
import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;
import com.cscie97.store.model.StoreModelServiceInterface;

import java.security.NoSuchAlgorithmException;

/**
 * Store controller that implements the store controller interface
 */
public class StoreController implements StoreControllerInterface {
	private static final StoreController instance = new StoreController();
	private StoreModelServiceInterface storeModelService;
	private AuthenticationServiceInterface authService;
	private LedgerInterface ledger;
	private static CommandFactory commandFactory;
	private String auth_token;

	private StoreController() {
		commandFactory = new CommandFactory();
	}

	/**
	 * Add an association with a store model service
	 * @param storeModelService to be controller by the controller
	 */
	public void addStoreModelService(StoreModelServiceInterface storeModelService) {
		this.storeModelService = storeModelService;
	}

	/**
	 * Add an association with the authentication service
	 * @param authService auth service singleton
	 */
	public void addAuthService(AuthenticationServiceInterface authService) {
		this.authService = authService;
		createControllerUser(authService);
	}

	/**
	 * Initializes the user log in for the controller user within the
	 * authentication service
	 * @param authService auth service singleton
	 */
	private void createControllerUser(AuthenticationServiceInterface authService){

		try {
			authService.createUser("controller", "controller");
			authService.addUserLogin("controller", "controller", "admin");
			this.auth_token = authService.userLogin("controller", "controller", "admin");
		} catch (NoSuchAlgorithmException | AccessDeniedException | InvalidAuthTokenException e){
			System.out.println(e);
		}
	}

	/**
	 * Add an association with a ledger service
	 * @param ledger ledger to keep track of transactions
	 */
	public void addLedger(LedgerInterface ledger) {
		this.ledger = ledger;
	}

	/**
	 * Instance method for the controller serivce
	 * @return instance of the service
	 */
	public static StoreControllerInterface getInstance() {
		return instance;
	}

	/**
	 * Implementation of the observer pattern method: update.
	 * 1. Receives information from the subject that calls the update method
	 * 2. Passes the information to the command factory
	 * 3. Executes the command
	 * @param storeId
	 * @param deviceId
	 * @param event
	 * @throws ObserverException
	 */
	public void update(String storeId, String deviceId, String event) throws ObserverException {
		try {
			Command command = commandFactory.getCommand(storeModelService, ledger, authService, storeId, deviceId, event, auth_token);
			command.execute();
			System.out.println("-- command successfully executed in response to event: " + event + " in " + storeId + "\n");
		} catch (StoreModelServiceException e) {
			throw new ObserverException("StoreModelServiceException: " + e.getAction(), e.getError());
		} catch (LedgerException e) {
			throw new ObserverException("LedgerServiceException: " + e.getAction(), e.getReason());
		} catch (AccessDeniedException e){
			throw new ObserverException("AccessDeniedException: " + e.getAction(), e.getError());
		} catch (InvalidAuthTokenException e){
			throw new ObserverException("InvalidAuthTokenException: " + e.getAction(), e.getError());
		}
	}
}
