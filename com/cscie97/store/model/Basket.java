package com.cscie97.store.model;

import java.util.HashMap;

/**
 * Basket is a representation of the items that the customer
 * has taken off the shelves of a store. Each basket is associated
 * with one customer.
 */
public class Basket {
	private String id;
	private HashMap<String, Integer> productCount;

	Basket(String id){
		this.id = id;
		productCount = new HashMap<String, Integer>();
	}

	/**
	 * Getter for counts of products within the basket
	 * @return product count
	 */
	public HashMap<String,Integer> getBasketItems() {
		return productCount;
	}

	/**
	 * Add product of a given quantity to the basket
	 * @param productId unique id
	 * @param count number to add to the basket
	 */
	void addProduct(String productId, int count){
		productCount.merge(productId, count, Integer::sum);
	}

	/**
	 * Remove products of a given quantity to the basket
	 * @param productId unique id
	 * @param count number to remove from the basket
	 * @throws StoreModelServiceException if too many items are removed or the product is not
	 * in the basket
	 */
	void removeProduct(String productId, int count) throws StoreModelServiceException {
		try {
			if (productCount.get(productId) >= count) {
				productCount.put(productId, productCount.get(productId) - count);
			}
			else {
				throw new StoreModelServiceException("removeBasketItem", "cannot remove more items than in the basket");
			}
		} catch (NullPointerException e) {
			throw new StoreModelServiceException("removeBasketItem", "product not in basket");
		}
	}

	/**
	 * Getter for the baskets unique id
	 * @return unique id of the basket
	 */
	public String getId(){
		return id;
	}

	/**
	 * Displays information about the basket
	 * @return informational string
	 */
	String show(){
		return "\n\tid: " + id +
				"\n\tproducts: " + productCount.toString() +
				"\n";
	}
}
