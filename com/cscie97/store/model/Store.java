package com.cscie97.store.model;

import java.util.HashMap;

/**
 * Representation of a single store within the 24x7 store system. Customers shop within stores,
 * and stores contain aisle, shelves, and devices. Stores have inventory.
 */
class Store {
	private String id;
	private String address;
	private String name;
	private int numberCurrentCustomers;
	private HashMap<String, Aisle> aisleMap;
	private HashMap<String, Inventory> inventoryMap;
	private HashMap<String, Device> deviceMap;

	Store(String id, String address, String name) {
		this.id = id;
		this.address = address;
		this.name = name;
		numberCurrentCustomers = 0;
		aisleMap = new HashMap<String, Aisle>();
		deviceMap = new HashMap<String, Device>();
		inventoryMap = new HashMap<String, Inventory>();
	}

	/**
	 * Add an aisle to the store
	 * @param aisle to add
	 */
	void addAisle(Aisle aisle) {
		aisleMap.put(aisle.getId(), aisle);
	}

	/**
	 * Getter for aisle
	 * @param aisleId unique id
	 * @return aisle object
	 * @throws StoreModelServiceException if aisle doesn't exist
	 */
	Aisle getAisle(String aisleId) throws StoreModelServiceException {
		if (aisleMap.get(aisleId) != null) {
			return aisleMap.get(aisleId);
		} else {
			throw new StoreModelServiceException("getAisle", "aisle not found");
		}
	}

	/**
	 * Add device to the store
	 * @param device to add
	 */
	void addDevice(Device device) {
		deviceMap.put(device.getId(), device);
	}

	/**
	 * Get a device by device id from the store
	 * @param deviceId unique id
	 * @return device object
	 * @throws StoreModelServiceException if device doesn't exist
	 */
	Device getDevice(String deviceId) throws StoreModelServiceException{
		if (deviceMap.get(deviceId) != null){
			return deviceMap.get(deviceId);
		} else {
			throw new StoreModelServiceException("getDevice", "device not found");
		}
	}

	/**
	 * Getter for the map of device ids to devices
	 * @return device map of device ids to devices
	 */
	HashMap<String, Device> getDeviceMap(){
		return deviceMap;
	}

	/**
	 * Getter for the map of aisle ids to aisle
	 * @return aisle map of ids to aisles
	 */
	HashMap<String, Aisle> getAisleMap(){
		return aisleMap;
	}

	/**
	 * Getter for the map of inventory id to inventory
	 * @return inventoryMap of ids to inventories
	 */
	HashMap<String, Inventory> getInventoryMap(){
		return inventoryMap;
	}

	/**
	 * Add inventory to the store
	 * @param inventory to add
	 */
	void addInventory(Inventory inventory) {
		inventoryMap.put(inventory.getId(), inventory);
	}

	/**
	 * Get inventory by id
	 * @param inventoryId unique id
	 * @return inventory object
	 * @throws StoreModelServiceException if inventory doesn't exist
	 */
	Inventory getInventory(String inventoryId) throws StoreModelServiceException{
		try {
			return inventoryMap.get(inventoryId);
		} catch (NullPointerException e){
			throw new StoreModelServiceException("getInventory", "inventory not found");
		}
	}

	/**
	 * Getter for the store id
	 * @return unique id
	 */
	String getId(){
		return id;
	}

	/**
	 * Getter for the number of customers in the store
	 * @return current customer number
	 */
	int getNumberCurrentCustomers(){
		return numberCurrentCustomers;
	}

	/**
	 * Reduce the number of customers by one
	 */
	void decrementNumberCurrentCustomers(){
		numberCurrentCustomers --;
	}

	/**
	 * Increment the number of customers by one
	 */
	void incrementNumberCurrentCustomers(){
		numberCurrentCustomers ++;
	}

	/**
	 * Display information about the store
	 * @return informational string
	 */
	String show(){
		StringBuilder aisleInfo = new StringBuilder();
		StringBuilder inventoryInfo = new StringBuilder();
		StringBuilder deviceInfo = new StringBuilder();

		for(Aisle aisle : aisleMap.values()){
			aisleInfo.append(aisle.show());
			aisleInfo.append("\n\t------------------------\n");
		}
		for(Inventory inventory : inventoryMap.values()){
			inventoryInfo.append(inventory.show());
			inventoryInfo.append("\n\t------------------------\n");
		}
		for(Device device : deviceMap.values()){
			deviceInfo.append(device.show());
			deviceInfo.append("\n\t------------------------\n");
		}

		return "\nname: " + name +
				"\naddress: " + address +
				"\nnumber of active customers: " + numberCurrentCustomers +
				"\naisles:\n"+ aisleInfo +
				"\ninventory:\n" + inventoryInfo +
				"\ndevices:\n" + deviceInfo +
				"\n";
	}
}
