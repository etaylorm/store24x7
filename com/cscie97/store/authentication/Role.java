package com.cscie97.store.authentication;

import java.util.ArrayList;

/**
 * Representation of a role within the authentication system. Roles extend entitlements and can contain other entitlements,
 * which can be roles, resource roles, or permissions.
 */
class Role extends Entitlement {
	private String id;
	private String name;
	private String description;
	private ArrayList<Entitlement> entitlements;

	Role(String id, String name, String description) {
		super(id, name, description);

		this.id = id;
		this.name = name;
		this.description = description;
		entitlements = new ArrayList<Entitlement>();
	}

	/**
	 * Accepts inventory and permission visitors
	 * @param visitor
	 */
	public void acceptVisitor(Visitor visitor) {
		visitor.visitEntitlement(this);
	}

	/**
	 * Returns a string builder with details about the role
	 * @return
	 */
	public StringBuilder show() {
		StringBuilder info = new StringBuilder("Role:" +
							"\n\tid: " + id +
							"\n\tname: " + name +
							"\n\tdescription: " + description);
		for (Entitlement entitlement : entitlements){
			info.append(entitlement.show());
		}
		return info;
	}

	/**
	 * Checks whether any of this role's entitlements have the requested permission on the resource
	 * @param permission permission to look for
	 * @param resource resource to check on
	 * @return
	 */
	public boolean hasPermission(String permission, String resource) {
		for (Entitlement entitlement : entitlements){
			if (entitlement.hasPermission(permission, resource)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Add an entitlement to the role
	 * @param entitlement to be added
	 */
	public void addEntitlement(Entitlement entitlement){
		entitlements.add(entitlement);
	}
}
