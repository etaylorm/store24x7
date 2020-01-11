package com.cscie97.store.model;

import com.cscie97.store.authentication.AccessDeniedException;
import com.cscie97.store.authentication.AuthenticationServiceInterface;
import com.cscie97.store.authentication.InvalidAuthTokenException;
import com.cscie97.store.controller.ObserverException;

import java.util.ArrayList;

/**
 * Public interface for the store model service which maintains the state of all the actors within
 * the 24x7 store service. The interface can be used for defining or setting up a store, simulating
 * events, updating the state of the domain objects within the store, and querying information about
 * the objects within the store.
 *
 * The store model service extends the subject interface. When events take place within the store,
 * the store model service notifies all observers.
 */
public interface StoreModelServiceInterface extends Subject {
	void notify(String storeId, String deviceId, String event) throws ObserverException;
	void register(Observer observer);
	void deregister(Observer observer);
	void updateCustomerBasket(String customerId, String productId, int itemCount, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void clearBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void createCommand(String storeId, String deviceId, String command, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void createEvent(String storeId, String deviceId, String event, String auth_token) throws ObserverException, AccessDeniedException, InvalidAuthTokenException;
	void defineAisle(String storeId, String number, String name, String description, String type, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineCustomer(String customerId, String firstName, String lastName, String type, String emailAddress, String accountId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineDevice(String deviceId, String name, String type, String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineInventory(String id, String storeId, String aisleNumber, String shelfId, int capacity, int count, String productId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineProduct(String productId, String name, String description, float weight, String category, float price, String temperature, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineProduct(String productId, String name, String description, float weight, String category, float price, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineShelf(String storeId, String aisleNumber, String id, String name, String level, String description, String temperature, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineShelf(String storeId, String aisleNumber, String id, String name, String level, String description, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void defineStore(String id, String name, String address, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void getCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	int tallyCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	float weighCustomerBasket(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showAisle(String storeId, String aisleNumber, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showBasketItems(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showCustomer(String CustomerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showDevice(String storeId, String deviceId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showInventory(String storeId, String inventoryId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showProduct(String productId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showShelf(String storeId, String aisleNumber, String shelfId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void showStore(String storeId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void updateCustomer(String customerId, String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void updateInventory(String storeId, String inventoryId, int incrementCount, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	String findNearestDevice(String storeId, String aisleId, String deviceType, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	String findDevice(String storeId, String deviceType, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	ArrayList<String> getDevices(String storeId, String deviceType, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	String getInventory(String storeId, String aisleId, String shelfId, String productId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void restock(String storeId, String inventoryId, int count, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	String findProduct(String storeId, String productId, int count, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	String findCustomerLocation(String customerId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void updateRobotLocation(String deviceId, String storeId, String aisleId, String auth_token) throws StoreModelServiceException, AccessDeniedException, InvalidAuthTokenException;
	void addAuthService(AuthenticationServiceInterface authenticationServiceInterface);
}