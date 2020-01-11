package com.cscie97.store.controller;

import com.cscie97.ledger.LedgerException;
import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.model.StoreModelServiceException;

/**
 * Command interface to be implemented by concrete command types
 * Has one method, execute, which completes the command
 */
public interface Command {
    void execute() throws StoreModelServiceException, LedgerException, ObserverException, AccessDeniedException, InvalidAuthTokenException;
}
