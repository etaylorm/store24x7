package com.cscie97.store.authentication;

/**
 * Access denied exception:
 * thrown when an auth token does not have the specified permission
 */
public class AccessDeniedException extends AuthenticationException {

    public AccessDeniedException(String action, String error) {
        super(action, error);
    }
}
