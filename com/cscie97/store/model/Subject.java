package com.cscie97.store.model;

import com.cscie97.store.controller.ObserverException;

/**
 * Implementation of the subject interface (for the observer design pattern). The subject interface
 * allows for observer objects to register and dereigster their interest in the subject. If an observer
 * is registered, it will be notified when events take place within the subject.
 */
public interface Subject {
    void notify(String storeId, String deviceId, String event) throws ObserverException;
    void register(Observer observer);
    void deregister(Observer observer);
}
