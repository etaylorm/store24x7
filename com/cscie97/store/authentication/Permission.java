package com.cscie97.store.authentication;

/**
 * Represents a restricted action that requires permission to do. The permission can be granted to users within the
 * authentication service.
 */
public class Permission extends Entitlement {
    private String id;
    private String name;
    private String description;

    Permission(String id, String name, String description) {
        super(id, name, description);
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Accepts both an inventory and permission visitor
     * @param visitor
     */
    public void acceptVisitor(Visitor visitor) {
        visitor.visitEntitlement(this);
    }

    /**
     * Details information about the permission
     * @return
     */
    public StringBuilder show() {
        return new StringBuilder("Permission:" +
                "\n\tid: " + id +
                "\n\tname: " + name +
                "\n\tdescription: " + description + "\n");
    }

    /**
     * Implementation of Entitlement's hasPermission method. For a permission, returns true
     * if the specified permission is equal to this permission.
     * @param permission permission to look for
     * @param resource resource to check on
     * @return true if the permission value is the same as the instance's
     */
    public boolean hasPermission(String permission, String resource) {
        return permission.equals(name);
    }
}
