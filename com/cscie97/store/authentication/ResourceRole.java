package com.cscie97.store.authentication;

import java.util.ArrayList;

/**
 * Extension of a role that is tied to a specifiec resource. Implements the entitlement interface due to extending
 * role
 */
public class ResourceRole extends Role {
	private String id;
	private String name;
	private String description;
	private ArrayList<Entitlement> entitlements;
	private Resource resource;

	ResourceRole(String id, String name, String description, Entitlement entitlement, Resource resource) {
		super(id, name, description);
		this.id = id;
		this.name = name;
		this.description = description;
		this.resource = resource;
		entitlements.add(entitlement);
	}

	/**
	 * Accepts inventory and permission visitors
	 * @param visitor
	 */
	public void acceptVisitor(Visitor visitor) {
		visitor.visitEntitlement(this);
	}

	/**
	 * Returns a string builder with details
	 * @return
	 */
	public StringBuilder show() {
		StringBuilder info = new StringBuilder("Resource role:" +
				"\n\tid: " + id +
				"\n\tname: " + name +
				"\n\tdescription: " + description +
				"\n\tresource: " + resource.getId());

		for (Entitlement entitlement : entitlements){
			info.append(entitlement.show());
		}
		return info;
	}

	/**
	 * Determines whether this role has the required permission. If the resource equals this instances resource,
	 * then check whether the permission is contained within the role. If both are true, return true.
	 * @param permission to check for
	 * @param resource in question
	 * @return
	 */
	public boolean hasPermission(String permission, String resource) {
		if (resource.equals(this.resource.getId())) {
			for (Entitlement entitlement : entitlements) {
				if (entitlement.hasPermission(permission, resource)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add an entitlement to the resource role
	 * @param entitlement to be added
	 */
	public void addEntitlement(Entitlement entitlement){
		entitlements.add(entitlement);
	}

}
