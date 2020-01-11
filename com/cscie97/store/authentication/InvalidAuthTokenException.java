package com.cscie97.store.authentication;

/**
 * Exception for invalid authentication tokens. This exception is raised if an auth token is passed that either
 * does not exist or has expired.
 */
public class InvalidAuthTokenException extends AuthenticationException {

    public InvalidAuthTokenException(String action, String error) {
        super(action, error);
    }
}
