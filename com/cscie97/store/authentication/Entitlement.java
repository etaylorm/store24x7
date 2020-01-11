package com.cscie97.store.authentication;

/**
 * Abstract class for an entitlement (role, resouce role, permission) within
 * the authentication service
 */
public abstract class Entitlement {
	private String id;
	private String name;
	private String description;

	Entitlement(String id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * Accepts a visitor and calls "visitEntitlement" on that visitor and passes a reference to itself
	 * @param visitor
	 */
	public void acceptVisitor(Visitor visitor) {
		visitor.visitEntitlement(this);
	}

	/**
	 * Show method that returns a string builder
	 * @return string builder
	 */
	public StringBuilder show() {
		return null;
	}

	/**
	 * Public method that checks whether this entitlement has the specified permission
	 * @param permission permission to look for
	 * @param resource resource to check on
	 * @return true if it has the permission
	 */
	public boolean hasPermission(String permission, String resource) {
		return false;
	}

	/**
	 * Add an entitlement to this entitlement (if role or resource role)
	 * @param entitlement to be added
	 */
	public void addEntitlement(Entitlement entitlement){ }

}
