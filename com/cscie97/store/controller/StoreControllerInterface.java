package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerInterface;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.Observer;
import com.cscie97.store.model.StoreModelServiceInterface;

import java.security.NoSuchAlgorithmException;

/**
 * Store controller interface that extends the observer interface
 */
public interface StoreControllerInterface extends Observer {
	void update(String storeId, String deviceId, String event) throws ObserverException;
	void addStoreModelService(StoreModelServiceInterface storeModelServiceInterface);
    void addLedger(LedgerInterface ledger);
    void addAuthService(AuthenticationServiceInterface authenticationServiceInterface);
}
