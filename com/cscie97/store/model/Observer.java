package com.cscie97.store.model;

import com.cscie97.store.controller.ObserverException;

/**
 * Observer interface for the observer pattern. Observers have one public method that is called by any
 * subjects they register interest in to notify the observer of changes within the subject
 */
public interface Observer {
    void update(String storeId, String deviceId, String event) throws ObserverException;
}
