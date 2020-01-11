package com.cscie97.store.model;

/**
 * Inventory represents the association of a type of product (with a count)
 * and a location within a given store. Each inventory instance is tied to
 * one type of product and one location.
 */
public class Inventory {
	private String id;
	private Location location;
	private String productId;
	private int count;
	private int capacity;

	public Inventory(String id, Location location, String productId, int capacity, int count)
			throws StoreModelServiceException{
		if (count > capacity){
			throw new StoreModelServiceException("defineInventory", "count exceeds capacity");
		}
		this.id = id;
		this.location = location;
		this.productId = productId;
		this.count = count;
		this.capacity = capacity;
	}

	/**
	 * Update the count of products for this inventory
	 * @param incrementCount
	 * @throws StoreModelServiceException if increase exceeds capacity or decrease goes below 0
	 */
	void updateInventory(int incrementCount) throws StoreModelServiceException {
		if(count + incrementCount >= 0 && count + incrementCount <= capacity){
			count = count + incrementCount;
		} else {
			throw new StoreModelServiceException("updateInventory", "inventory at capacity");
		}
	}

	/**
	 * Getter for productID
	 * @return productID
	 */
	String getProductId(){
		return productId;
	}

	/**
	 * Getter for location of the inventory
	 * @return location
	 */
	Location getLocation(){
		return location;
	}

	/**
	 * Getter for the number of products currently in the inventory
	 * @return current product count
	 */
	int getCount(){
		return count;
	}

	/**
	 * Display information about the inventory
	 * @return string with details about inventory
	 */
	String show(){
		return "\n\tid: " + id +
				"\n\tlocation: " + location +
				"\n\tproductId: " + productId +
				"\n\tcount/capacity: " + count + "/" + capacity +
				"\n";
	}

	/**
	 * Getter for the inventory id
	 * @return id
	 */
	String getId(){
		return id;
	}

}
