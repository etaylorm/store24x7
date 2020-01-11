package com.cscie97.store.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Customer is a representation of a single customer that is known
 * to the 24x7 store system. Customers can be registered or guests and can
 * shop at any of the 24x7 stores with their associated payment account.
 * Customers have associated baskets with items that they intend to purchase.
 */
public class Customer {
	private String id;
	private String firstName;
	private String lastName;
	private String type;
	private String email;
	private String accountId;
	private Location location;
	private LocalTime lastSeen;
	private Basket basket;

	public Customer(String id, String firstName, String lastName, String type, String email, String accountId) throws StoreModelServiceException {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.accountId = accountId;

		ArrayList<String> types = new ArrayList<>(Arrays.asList("registered", "guest")); // customers can be registered or guests

		if (types.contains(type)){
			this.type = type;
		} else {
			throw new StoreModelServiceException("defineCustomer", "customer type not recognized");
		}
	}

	/**
	 * Returns the customer's basket (of which the customer can have one at a time)
	 * or creates a new basket for the customer
	 * @return
	 */
	Basket getBasket(){
		if (basket == null & type.equals("registered")){
			createBasket();
		}
		return basket;
	}

	/**
	 * Clears the customer's association with their basket
	 */
	void clearBasket(){
		basket = null;
	}

	/**
	 * Creates a new basket for the customer
	 */
	private void createBasket(){
		basket = new Basket("basket_" + id);
	}

	/**
	 * Updates the customer's current location
	 * and updates the timestamp of when they were last seen
	 * @param location
	 */
	void updateLocation(Location location){
		this.location = location;
		lastSeen = LocalTime.now();
	}

	/**
	 * Getter for the customer's location
	 * @return location of the customer
	 */
	Location getLocation(){
		return location;
	}

	/**
	 * Getter for the time the customer was last seen
	 * @return timestamp that the customer was last seen
	 */
	LocalTime getLastSeen(){
		return lastSeen;
	}

	/**
	 * Show information about the customer
	 * @return informational string
	 */
	String show(){
		String info = "\nid: " + id +
				"\nname: " + firstName + " " + lastName +
				"\nlocation: " + location +
				"\ntype: " + type +
				"\nemail: " + email +
				"\naccount: " + accountId +
				"\nbasket: ";

		if(basket != null){
			info += basket.show();
		} else {
			info += "no basket";
		}
		info += "\n";

		return info;
	}
}
