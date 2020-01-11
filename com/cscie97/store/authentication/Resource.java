package com.cscie97.store.authentication;

/**
 * Represents a resource that can have restricted access.
 */
public class Resource {
	private String id;
	private String description;

	Resource(String id, String description){
		this.id = id;
		this.description = description;
	}

	/**
	 * Accepts inventory and permission visitors
	 * @param visitor
	 */
	public void acceptVisitor(Visitor visitor) {
		visitor.visitResource(this);
	}

	/**
	 * Details the resource
	 * @return string builder
	 */
	public StringBuilder show() {
		return new StringBuilder("Resource:" +
							"\n\tid: " + id +
							"\n\tdescription: " + description);
	}

	/**
	 * Getter for the resource id
	 * @return id
	 */
	String getId(){
		return id;
	}

}
