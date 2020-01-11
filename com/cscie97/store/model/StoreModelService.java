package com.cscie97.store.model;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.controller.ObserverException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements the store model service interface, the API for the store model service.
 * The store model service allows the store controller (and an admin) to define, show details of,
 * monitor, and control elements of a store.
 */
public class StoreModelService implements StoreModelServiceInterface {
	private static final StoreModelService instance = new StoreModelService();

	private AuthenticationServiceInterface authService;
	private HashMap<String, Store> storeMap;
	private HashMap<String, Customer> customerMap;
	private HashMap<String, Product> productMap;
	private ArrayList<Observer> observers;

	private StoreModelService(){
		storeMap = new HashMap<String, Store>();
		customerMap = new HashMap<String, Customer>();
		productMap = new HashMap<String, Product>();
		observers = new ArrayList<Observer>();
	}

	/**
	 * Return singleton instance of the store model service
	 * @return store model service interface
	 */
	public static StoreModelServiceInterface getInstance(){
		return instance;
	}

	/**
	 * Add an association with the authentication service
	 * @param authService
	 */
	public void addAuthService(AuthenticationServiceInterface authService){
		this.authService = authService;
	}

	/**
	 * Define new store
	 * @param id
	 * @param name
	 * @param address
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineStore(String id, String name, String address, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", null);
		validateId(storeMap, id, "defineStore");
		Store store = new Store(id, name, address);
		storeMap.put(id, store);
	}

	/**
	 * Show details of a store
	 * @param storeId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showStore(String storeId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showstoreobject", storeId);
		System.out.println(getStore(storeId).show());
	}

	/**
	 * Define a new aisle within a store
	 * @param storeId
	 * @param id
	 * @param name
	 * @param description
	 * @param type
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineAisle(String storeId, String id, String name,
							String description, String type, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", storeId);
		validateId(getStore(storeId).getAisleMap(), id, "defineAisle");
		Aisle aisle = new Aisle(id, name, description, type);
		getStore(storeId).addAisle(aisle);
	}

	/**
	 * Show details of an aisle
	 * @param storeId
	 * @param aisleId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showAisle(String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showstoreobject", storeId);
		System.out.println(getStore(storeId).getAisle(aisleId).show());
	}

	/**
	 * Define a new shelf within a store and aisle
	 * @param storeId
	 * @param aisleId
	 * @param id
	 * @param name
	 * @param level
	 * @param description
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineShelf(String storeId, String aisleId, String id, String name,
							String level, String description, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", storeId);
		validateId(getStore(storeId).getAisle(aisleId).getShelfMap(), id, "defineShelf");
		Shelf shelf = new Shelf(id, name, level, description);
		getStore(storeId).getAisle(aisleId).addShelf(shelf);
	}

	/**
	 * Define a shelf within a store and aisle with a specific temperature
	 * @param storeId
	 * @param aisleId
	 * @param id
	 * @param name
	 * @param level
	 * @param description
	 * @param temperature
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineShelf(String storeId, String aisleId, String id, String name,
							String level, String description, String temperature, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", storeId);
		validateId(getStore(storeId).getAisle(aisleId).getShelfMap(), id, "defineShelf");
		Shelf shelf = new Shelf(id, name, level, description, temperature);
		getStore(storeId).getAisle(aisleId).addShelf(shelf);
	}

	/**
	 * Show details of a given shelf
	 * @param storeId
	 * @param aisleId
	 * @param shelfId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showShelf(String storeId, String aisleId, String shelfId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showstoreobject", storeId);
		System.out.println(getStore(storeId).getAisle(aisleId).getShelf(shelfId).show());
	}

	/**
	 * Define new inventory within a store
	 * @param id
	 * @param storeId
	 * @param aisleId
	 * @param shelfId
	 * @param capacity
	 * @param count
	 * @param productId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineInventory(String id, String storeId, String aisleId, String shelfId, int capacity,
								int count, String productId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", storeId);
		validateId(getStore(storeId).getInventoryMap(), id, "defineInventory");
		Location location = new Location(getStore(storeId),
				getStore(storeId).getAisle(aisleId),
				getStore(storeId).getAisle(aisleId).getShelf(shelfId));
		Inventory inventory = new Inventory(id, location, productId, capacity, count);
		getStore(storeId).addInventory(inventory);
	}

	/**
	 * Show details of a specific inventory
	 * @param storeId
	 * @param inventoryId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showInventory(String storeId, String inventoryId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showstoreobject", storeId);
		try {
			System.out.println(getStore(storeId).getInventory(inventoryId).show());
		} catch (NullPointerException e) {
			throw new StoreModelServiceException("showInventory", "inventory not found");
		}
	}

	/**
	 * Update the count of products within a given inventory (increase or decrease)
	 * @param storeId
	 * @param inventoryId
	 * @param incrementCount
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void updateInventory(String storeId, String inventoryId, int incrementCount, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "updatestoremodel", storeId);
		try {
			getStore(storeId).getInventory(inventoryId).updateInventory(incrementCount);
		} catch (NullPointerException e){
			throw new StoreModelServiceException("updateInventory", "inventory not found");
		}
	}

	/**
	 * Create a new customer within the store model system
	 * @param customerId
	 * @param firstName
	 * @param lastName
	 * @param type
	 * @param emailAddress
	 * @param accountId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineCustomer(String customerId, String firstName, String lastName,
							   String type, String emailAddress, String accountId, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definecustomer", null);
		Customer customer = new Customer(customerId, firstName, lastName, type, emailAddress, accountId);
		validateId(customerMap, customerId, "defineCustomer");
		customerMap.put(customerId, customer);
	}

	/**
	 * Show details of a given customer
	 * @param customerId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showCustomer(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showcustomer", null);
		System.out.println(getCustomer(customerId).show());
	}

	/**
	 * Update the location of a customer, and update the counts of customers for the store they left (if any)
	 * and the store they entered
	 * @param customerId
	 * @param storeId
	 * @param aisleId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void updateCustomer(String customerId, String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "updatecustomer", null);
		Location lastSeen = getCustomer(customerId).getLocation();
		Location newLocation = new Location(getStore(storeId), getStore(storeId).getAisle(aisleId));

		if (lastSeen != null && lastSeen.getStore() != newLocation.getStore()){
			lastSeen.getStore().decrementNumberCurrentCustomers();
		}
		newLocation.getStore().incrementNumberCurrentCustomers();

		getCustomer(customerId).updateLocation(newLocation);
	}

	/**
	 * Get the id of a customers basket
	 * @param customerId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void getCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "getcustomerbasket", null);
		getCustomer(customerId).getBasket().getId();
	}

	/**
	 * Add a product of a given number to a customer's basket
	 * @param customerId
	 * @param productId
	 * @param itemCount
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void updateCustomerBasket(String customerId, String productId, int itemCount, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "updatecustomerbasket", null);
		getProduct(productId);
		if (itemCount > 0) {
			getCustomer(customerId).getBasket().addProduct(productId, itemCount);
		} else {
			getCustomer(customerId).getBasket().removeProduct(productId, itemCount);
		}
	}

	/**
	 * Remove items and strip customer of their basket
	 * @param customerId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void clearBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "clearcustomerbasket", null);
		getCustomer(customerId).clearBasket();
	}

	/**
	 * Display product count within a customers basket
	 * @param customerId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showBasketItems(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showcustomer", null);
		System.out.println(getCustomer(customerId).getBasket().show());
	}

	/**
	 * Create a new product within the store model service system
	 * @param productId
	 * @param name
	 * @param description
	 * @param weight
	 * @param category
	 * @param price
	 * @param temperature
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineProduct(String productId, String name, String description,
							  float weight, String category, float price, String temperature, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "defineproduct", null);
		validateId(productMap, productId, "defineProduct");
		Product product = new Product(productId, name, description, weight, category, price, temperature);
		productMap.put(productId, product);
	}

	/**
	 * Define a new product within the store model service system without temperature
	 * @param productId
	 * @param name
	 * @param description
	 * @param weight
	 * @param category
	 * @param price
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineProduct(String productId, String name, String description,
							  float weight, String category, float price, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "defineproduct", null);
		validateId(productMap, productId, "defineProduct");
		Product product = new Product(productId, name, description, weight, category, price);
		productMap.put(productId, product);
	}

	/**
	 * Display details of a product
	 * @param productId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showProduct(String productId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showproduct", null);
		System.out.println(getProduct(productId).show());
	}

	/**
	 * Create a new device, by type
	 * @param deviceId
	 * @param name
	 * @param type
	 * @param storeId
	 * @param aisleId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void defineDevice(String deviceId, String name, String type, String storeId, String aisleId, String auth_token)
			throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "definestoreobject", storeId);
		validateId(getStore(storeId).getDeviceMap(), deviceId, "defineDevice");
		Location location = new Location(getStore(storeId), getStore(storeId).getAisle(aisleId));
		Device device;
		switch(type){
			case("microphone"):
				device = new Device(deviceId, name, "microphone", location); // cameras and microphones are sensors, or base devices
				getStore(storeId).addDevice(device);
				break;
			case("camera"):
				device = new Device(deviceId, name, "camera", location); // cameras and microphones are sensors, or base devices
				getStore(storeId).addDevice(device);
				break;
			case("speaker"):
				device = new Appliance(deviceId, name, "speaker", location); // speakers are appliances because they can process commands
				getStore(storeId).addDevice(device);
				break;
			case("robot"):
				device = new Robot(deviceId, name, "robot", location); // robots are a type of appliance
				getStore(storeId).addDevice(device);
				break;
			case("turnstile"):
				device = new Turnstile(deviceId, name, "turnstile", location); // turnstiles are a type of appliance
				getStore(storeId).addDevice(device);
				break;
			default:
				throw new StoreModelServiceException("definedevice", "device type not recognized");
		}
	}

	/**
	 * Show details of a device
	 * @param storeId
	 * @param deviceId
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void showDevice(String storeId, String deviceId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "showstoreobject", storeId);
		System.out.println(getStore(storeId).getDevice(deviceId).show());
	}

	/**
	 * Simulate the creation of an event
	 * @param storeId
	 * @param deviceId
	 * @param event
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void createEvent(String storeId, String deviceId, String event, String auth_token) throws ObserverException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "createevent", storeId);
		notify(storeId, deviceId, event);
	}

	/**
	 * Issue a command to a device
	 * @param storeId
	 * @param deviceId
	 * @param command
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void createCommand(String storeId, String deviceId, String command, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "createcommand", storeId);
		getStore(storeId).getDevice(deviceId).issueCommand(command);
	}

	/**
	 * Alerts all registered observers of an event
	 * @param event state-change that the observers need to be told about
	 */
	public void notify(String storeId, String deviceId, String event) throws ObserverException {
		for (Observer observer: observers){
			observer.update(storeId, deviceId, event);
		}
	}

	/**
	 * Called by an observer wishing to be notified of any changes
	 * @param observer object with an update method that wishes to be notified of events
	 */
	public void register(Observer observer){
		observers.add(observer);
	}

	/**
	 * Called by a current observer wishing to no longer receive notifications
	 * @param observer observer wishing to no longer be notified
	 */
	public void deregister(Observer observer){
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	/**
	 * Searches through the devices within a store and returns the id of the device of the specified
	 * type in the specified aisle. If no device of that type is within the given aisle, a deviceId for
	 * a device of the specified type somewhere in the store is returned. If no device of the given type
	 * exists in the store, returns null
	 * @param storeId store to search within
	 * @param aisleId preferred aisle in which to find a device
	 * @param deviceType type of device requested
	 * @param auth_token
	 * @return deviceId or null
	 * @throws StoreModelServiceException
	 */
	public String findNearestDevice(String storeId,
									String aisleId,
									String deviceType,
									String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", storeId);

		HashMap<String, Device> deviceMap = getStore(storeId).getDeviceMap();

		String backupDeviceId = null;
		for (String deviceId: deviceMap.keySet()){
			if (getStore(storeId).getDevice(deviceId).getType().equals(deviceType)){
				backupDeviceId = deviceId;

				if (getStore(storeId).getDevice(deviceId).getLocation().getAisle().getId().equals(aisleId)){
					return deviceId;
				}
			}
		}
		if (backupDeviceId != null){
			return backupDeviceId;
		} else {
			throw new StoreModelServiceException("findDevice", "no device of type " + deviceType);
		}
	}

	/**
	 * Find a device of a given type within a store
	 * @param storeId store to look within
	 * @param deviceType device type to look for
	 * @param auth_token
	 * @return
	 * @throws StoreModelServiceException
	 */
	public String findDevice(String storeId, String deviceType, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", storeId);

		HashMap<String, Device> deviceMap = getStore(storeId).getDeviceMap();
		for (String deviceId: deviceMap.keySet()){
			if (getStore(storeId).getDevice(deviceId).getType().equals(deviceType)){
				return deviceId;
			}
		}
		throw new StoreModelServiceException("findDevice", "no device of type " + deviceType);
	}

	/**
	 * Weighs the items in a customer's basket and returns the total weight
	 * @param customerId customer whose basket to weigh
	 * @param auth_token
	 * @return
	 * @throws StoreModelServiceException
	 */
	public float weighCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", null);

		HashMap<String, Integer> basketItems = getCustomer(customerId).getBasket().getBasketItems();
		float weight = 0;
		for (String productId: basketItems.keySet()){
			weight += getProduct(productId).getWeight();
		}
		return weight;
	}

	/**
	 * Returns a list of deviceIds within a specified store of a given type of device
	 * @param storeId store to look within for devices
	 * @param deviceType type of device to return (microphone, robot, speaker, camera, turnstile)
	 * @param auth_token
	 * @return list of device IDs
	 * @throws StoreModelServiceException
	 */
	public ArrayList<String> getDevices(String storeId,
										String deviceType,
										String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", storeId);

		ArrayList<String> devices = new ArrayList<String>();

		// get list of all devices within the specified store
		HashMap<String, Device> deviceMap = getStore(storeId).getDeviceMap();

		// for each device, if its of the desired type, add its id to the returned list
		for (String deviceId : deviceMap.keySet()) {
			if (getStore(storeId).getDevice(deviceId).getType().equals(deviceType)){
				devices.add(deviceId);
			}
		}
		return devices;
	}

	/**
	 * Sums up the prices of all the items within a given customer's basket in preparation for checkout
	 * @param customerId customer whose basket to tally
	 * @param auth_token
	 * @return total cost of items in the basket
	 * @throws StoreModelServiceException
	 */
	public int tallyCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", null);
		HashMap<String, Integer> basketItems = getCustomer(customerId).getBasket().getBasketItems();
		int total = 0;
		for (String productId : basketItems.keySet()){
			total += basketItems.get(productId) * getProduct(productId).getPrice();
		}
		return total;
	}

	/**
	 * Returns the inventory id for a given location and product. Returns null if no inventory by the given
	 * description exists
	 * @param storeId store to look within
	 * @param aisleId aisle to look within
	 * @param shelfId shelf of the inventory
	 * @param productId product of inventory
	 * @param auth_token
	 * @return inventoryId or null if inventory does not exist
	 * @throws StoreModelServiceException
	 */
	public String getInventory(String storeId,
							   String aisleId,
							   String shelfId,
							   String productId,
							   String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", storeId);

		HashMap<String, Inventory> inventoryMap = getStore(storeId).getInventoryMap();
		for (String inventoryId: inventoryMap.keySet()){
			if (inventoryMap.get(inventoryId).getLocation().getAisle().getId().equals(aisleId) &
					inventoryMap.get(inventoryId).getLocation().getShelf().getId().equals(shelfId) &
					inventoryMap.get(inventoryId).getProductId().equals(productId)){
				return inventoryId;
			}
		}
		return null;
	}

	/**
	 * If there are products in the storeroom, restock the inventory on the floor using the storeroom inventory.
	 * If there aren't products in the storeroom, do nothing.
	 * @param storeId store to look for inventory within
	 * @param inventoryId inventory that was reduced on the floor
	 * @param count amount of product that was removed
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void restock(String storeId,
						String inventoryId,
						int count,
						String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "updatestoremodel", storeId);

		HashMap<String, Inventory> inventoryMap = getStore(storeId).getInventoryMap();
		Inventory inventory = getStore(storeId).getInventory(inventoryId);
		for (String id : inventoryMap.keySet()) {
			if (inventoryMap.get(id).getProductId().equals(inventory.getProductId()) &
					inventoryMap.get(id).getLocation().getAisle().getType().equals("storeroom")){
				updateInventory(storeId, id, -1 * count, auth_token);
				updateInventory(storeId, inventoryId, count, auth_token);
				showInventory(storeId, inventoryId, auth_token);
			}
		}
	}

	/**
	 * Find inventory that contains the given product within a specified store
	 * @param storeId store to look within
	 * @param productId product to look for
	 * @param count number of products to find
	 * @param auth_token
	 * @return id of inventory within the store that has enough products
	 * @throws StoreModelServiceException
	 */
	public String findProduct(String storeId, String productId, int count, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", storeId);
		HashMap<String, Inventory> inventoryMap = getStore(storeId).getInventoryMap();
		for (String inventoryId : inventoryMap.keySet()) {
			if (inventoryMap.get(inventoryId).getProductId().equals(productId) &
					inventoryMap.get(inventoryId).getCount() >= count) {
				return inventoryId;
			}
		}
		return null;
	}

	/**
	 * Returns the customer's current location
	 * @param customerId id of the customer to find
	 * @param auth_token
	 * @return string location
	 * @throws StoreModelServiceException
	 */
	public String findCustomerLocation(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "querystoremodel", null);
		return getCustomer(customerId).getLocation().toString();
	}

	/**
	 * Update the robot's location based on a task assigned to it
	 * @param deviceId id of the robot
	 * @param storeId id of the store the robot has moved to
	 * @param aisleId id of the aisle the robot has moved to
	 * @param auth_token
	 * @throws StoreModelServiceException
	 */
	public void updateRobotLocation(String deviceId, String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException {
		validateAuthToken(auth_token, "updatestoremodel", storeId);
		getStore(storeId).getDevice(deviceId).updateLocation(new Location(getStore(storeId), getStore(storeId).getAisle(aisleId)));
	}

	/**
	 * Helper function to return reference to a given store or raise an exception if the store isn't found
	 * @param storeId
	 * @return
	 * @throws StoreModelServiceException
	 */
	private Store getStore(String storeId) throws StoreModelServiceException{
		if (storeMap.get(storeId) != null){
			return storeMap.get(storeId);
		} else {
			throw new StoreModelServiceException("getStore", "store not found");
		}
	}

	/**
	 * Helper function to return reference to a customer or raise an exception
	 * @param customerId
	 * @return
	 * @throws StoreModelServiceException
	 */
	private Customer getCustomer(String customerId) throws StoreModelServiceException{
		if (customerMap.get(customerId) != null){
			return customerMap.get(customerId);
		} else {
			throw new StoreModelServiceException("getCustomer", "customer not found");
		}
	}

	/**
	 * Helper function to return reference to a product or raise exception
	 * @param productId
	 * @return
	 * @throws StoreModelServiceException
	 */
	private Product getProduct(String productId) throws StoreModelServiceException{
		if (productMap.get(productId) != null){
			return productMap.get(productId);
		} else {
			throw new StoreModelServiceException("getProduct", "product not found");
		}
	}

	/**
	 * Helper function to validate whether an id already exists within a given map and
	 * return the appropriate exception if it does
	 * @param map
	 * @param id
	 * @param action
	 * @throws StoreModelServiceException
	 */
	private void validateId(HashMap map, String id, String action) throws StoreModelServiceException {
		if (map.get(id) != null){
			throw new StoreModelServiceException(action, "id is not unique");
		}
	}

	/**
	 * Make call to the authentication service to validate that the user with the given
	 * auth token has permission to do the action specified on the resource (if relevant)
	 * @param authToken auth token for the user
	 * @param action restricted action attempted
	 * @param resource resource on which the action was attempted, if relevant
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	private void validateAuthToken(String authToken, String action, String resource) throws AccessDeniedException, InvalidAuthTokenException {
		authService.checkPermission(authToken, action, resource);
	}
}
