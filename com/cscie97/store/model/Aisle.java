package com.cscie97.store.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Representation of an aisle within a store, on the floor
 * or storeroom
 */
public class Aisle {
	private String id;
	private String name;
	private String description;
	private String type;
	private HashMap<String, Shelf> shelfMap;

	public Aisle(String id, String name, String description, String type) throws StoreModelServiceException {
		this.id = id;
		this.name = name;
		this.description = description;
		shelfMap = new HashMap<String, Shelf>();

		ArrayList<String> types = new ArrayList<>(Arrays.asList("floor", "storeroom"));

		if (types.contains(type)){
			this.type = type;
		} else {
			throw new StoreModelServiceException("defineAisle", "aisle type not recognized");
		}
	}

	/**
	 * Adds a shelf to the aisle
	 * @param shelf reference to the shelf
	 */
	void addShelf(Shelf shelf){
		shelfMap.put(shelf.getId(), shelf);
	}

	/**
	 * Getter for shelf
	 * @param shelfId unique id for the shelf
	 * @return shelf reference to shelf
	 * @throws StoreModelServiceException
	 */
	Shelf getShelf(String shelfId) throws StoreModelServiceException{
		if (shelfMap.get(shelfId) != null) {
			return shelfMap.get(shelfId);
		} else {
			throw new StoreModelServiceException("getShelf", "shelf not found");
		}
	}

	/**
	 * Getter for the aisle's shelf map
	 * @return shelfMap map of shelf ids to the shelf object
	 */
	HashMap<String, Shelf> getShelfMap(){
		return shelfMap;
	}

	/**
	 * Getter for the aisles unique id
	 * @return id of the aisle
	 */
	public String getId(){
		return id;
	}

	/**
	 * Getter for the aisle's type (floor
	 * or storeroom)
	 * @return type of the aisle
	 */
	public String getType(){
		return type;
	}

	/**
	 * Getter for the description of the aisle
	 * @return description
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Returns information about the aisle and the
	 * shelves within in
	 * @return information string
	 */
	String show(){
		return "\tid: " + id +
				"\n\tname: " + name +
				"\n\tdescription: " + description +
				"\n\ttype: " + type +
				"\n\tshelves: " + shelfMap.keySet().toString();
	}
}
