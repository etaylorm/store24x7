package com.cscie97.store.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representation of a single type of product sold within the 24x7 store system
 */
public class Product {
	private String id;
	private String name;
	private String description;
	private float weight;
	private String category;
	private float price;
	private String temperature;

	public Product(String id, String name, String description, float weight, String category, float price, String temperature) throws StoreModelServiceException {
		this.id = id;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.category = category;
		this.price = price;

		// products can have one of the following list of temperatures
		ArrayList<String> temperatures = new ArrayList<>(Arrays.asList("frozen", "refrigerated", "ambient", "warm", "hot"));

		if (temperatures.contains(temperature.toLowerCase())){
			this.temperature = temperature.toLowerCase();
		} else {
			throw new StoreModelServiceException("defineProduct", "temperature not recognized");
		}
	}

	public Product(String id, String name, String description, float weight, String category, float price){
		this.id = id;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.category = category;
		this.price = price;
		this.temperature = "ambient"; // default value
	}

	/**
	 * Getter for product id
	 * @return id
	 */
	public String getId(){
		return id;
	}

	/**
	 * Getter for temperature
	 * @return temperature
	 */
	public String getTemperature(){
		return temperature;
	}

	/**
	 * Getter for price
	 * @return price
	 */
	public float getPrice(){
		return price;
	}

	/**
	 * Getter for category
	 * @return category of product
	 */
	public String getCategory(){
		return category;
	}

	/**
	 * Getter for weight
	 * @return weight of the object
	 */
	public float getWeight(){
		return weight;
	}

	/**
	 * Display the attributes of the product
	 * @return string with info about product
	 */
	String show(){
		return "\n\tid: " + id +
				"\n\tname: " + name +
				"\n\tdescription: " + description +
				"\n\tweight: " + weight +
				"\n\tcategory: " + category +
				"\n\tprice: " + price +
				"\n\ttemperature: " + temperature +
				"\n";
	}
}
