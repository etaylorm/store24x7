package com.cscie97.store.authentication;

/**
 * Visitor that takes inventory of the items within the authentication service
 */
public class InventoryVisitor implements Visitor {
	private StringBuilder inventory;

	/**
	 * Constructor initalizes a string builder
	 */
	InventoryVisitor(){
		this.inventory = new StringBuilder("Inventory:\n\t");
	}

	/**
	 * When visiting the authentication service, the inventory visitor visits every user, resource, and entitlement
	 * within the authentication service
	 * @param authService
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	public void visitAuthenticationService(AuthenticationService authService) throws AccessDeniedException, InvalidAuthTokenException {
		for (User user : authService.getUsers()){
			user.acceptVisitor(this);
		}
		for (Resource resource: authService.getResources()){
			resource.acceptVisitor(this);
		}
		for (Entitlement entitlement: authService.getEntitlements()){
			entitlement.acceptVisitor(this);
		}
	}

	/**
	 * The inventory visitor calls the user's show method and adds it to the inventory string
	 * @param user user that was visited
	 */
	public void visitUser(User user) {
		inventory.append(user.show());
	}

	/**
	 * Calls the resources' show method and adds it to the string
	 * @param resource that was visited
	 */
	public void visitResource(Resource resource) {
		inventory.append(resource.show());
	}

	/**
	 * Calls the entitlement's show method and adds string to the inventory
	 * @param entitlement to be visited
	 */
	public void visitEntitlement(Entitlement entitlement) {
		inventory.append(entitlement.show());
	}

	/**
	 * Public method to get the final inventory string
	 * @return fully-built inventory string
	 */
	public StringBuilder getInventory(){
		return inventory;
	}
}
