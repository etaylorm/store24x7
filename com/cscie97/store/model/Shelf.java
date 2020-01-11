package com.cscie97.store.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representation of a single shelf within an aisle, within a store
 */
public class Shelf {
	private String id;
	private String name;
	private String level;
	private String description;
	private String temperature;

	public Shelf(String id, String name, String level, String description, String temperature) throws StoreModelServiceException {
		this.id = id;
		this.name = name;
		this.level = level;
		this.description = description;

		// shelves can have one of a few temperatures
		ArrayList<String> temperatures = new ArrayList<>(Arrays.asList("frozen", "refrigerated", "ambient", "warm", "hot"));

		if (temperatures.contains(temperature.toLowerCase())){
			this.temperature = temperature.toLowerCase();
		} else {
			throw new StoreModelServiceException("defineShelf", "temperature not recognized");
		}
		this.temperature = temperature;
	}

	public Shelf(String id, String name, String level, String description){
		this.id = id;
		this.name = name;
		this.level = level;
		this.description = description;
		this.temperature = "ambient"; // default
	}

	/**
	 * Getter for id
	 * @return id for the shelf
	 */
	public String getId(){
		return id;
	}

	/**
	 * Display information about the shelf
	 * @return information string
	 */
	String show() {
		return "\n\tid: " + id +
				"\n\tname: " + name +
				"\n\tdescription: " + description +
				"\n\tlevel: " + level +
				"\n\ttemperature: " + temperature +
				"\n";
	}
}
