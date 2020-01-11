package com.cscie97.store.model;

/**
 * The location class holds references to the three physical elements
 * within a store: store, aisle, and shelf. Location is used to tie other objects
 * such as inventory and device, to a given place.
 */
public class Location {
    private Store store;
    private Aisle aisle;
    private Shelf shelf;

    Location(Store store, Aisle aisle){
        this.store = store;
        this.aisle = aisle;
    }

    Location(Store store, Aisle aisle, Shelf shelf){ // location can be specified with or without shelf
        this.store = store;
        this.aisle = aisle;
        this.shelf = shelf;
    }

    /**
     * Getter for store
     * @return store of location
     */
    Store getStore(){
        return store;
    }

    /**
     * Getter for aisle
     * @return aisle of location
     */
    Aisle getAisle(){
        return aisle;
    }

    /**
     * Getter for shelf
     * @return shelf of location
     */
    Shelf getShelf(){
        return shelf;
    }

    @Override
    public String toString() {
        if (shelf != null){
            return store.getId() + ":" + aisle.getId() + ":" + shelf.getId();
        } else {
            return store.getId() + ":" + aisle.getId();
        }
    }
}
