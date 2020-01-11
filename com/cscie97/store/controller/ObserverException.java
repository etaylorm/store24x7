package com.cscie97.store.controller;

/**
 * Exception class for the observer class
 */
public class ObserverException extends Exception {
    private String action;
    private String error;

    ObserverException(String action, String error) {
        super(action + " error: " + error);
    }
}
