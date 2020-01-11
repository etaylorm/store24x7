package com.cscie97.store.authentication;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Implementation of the Authentication Service interface
 * - Allows for the creation of users, resources, and entitlements
 * - Allows for entitlements to be associated with users
 * - Provides authentication through auth tokens generate for users using
 * 	log in or bio prints (face and voice)
 */
public class AuthenticationService implements AuthenticationServiceInterface {
	private static final AuthenticationService instance = new AuthenticationService();

	private HashMap<String, Resource> resources = new HashMap<>();
	private HashMap<String, Entitlement> entitlements = new HashMap<>();
	private HashMap<String, User> users = new HashMap<>();

	/**
	 * Return singleton instance of the store model service
	 * @return store model service interface
	 */
	public static AuthenticationServiceInterface getInstance(){
		return instance;
	}

	/**
	 * Create a new permission and add to list of entitlements
	 * @param id
	 * @param name
	 * @param description
	 */
	public void definePermission(String id, String name, String description) {
		entitlements.put(id, new Permission(id, name, description));
	}

	/**
	 * Create a new resource and add to list of resources
	 * @param id
	 * @param description
	 */
	public void defineResource(String id, String description) {
		resources.put(id, new Resource(id, description));
	}

	/**
	 * Create new resource role and add to list of entitlements
	 * @param id
	 * @param name
	 * @param description
	 * @param role role to bind to the resource
	 * @param resource resource to bind with the role
	 */
	public void defineResourceRole(String id, String name, String description, String role, String resource) {
		entitlements.put(id,
				new ResourceRole(id, name, description, entitlements.get(role), resources.get(resource)));
	}

	/**
	 * Create a new role and add to list of entitlements
	 * @param id
	 * @param name
	 * @param description
	 */
	public void defineRole(String id, String name, String description) {
		entitlements.put(id, new Role(id, name, description));
	}

	/**
	 * Create new user within the auth service and add to user list
	 * @param id
	 * @param name
	 */
	public void createUser(String id, String name) {
		users.put(id, new User(id, name));
	}

	/**
	 * Add a user bio print (face or voice)
	 * @param userId
	 * @param userPrint string representation of the print
	 */
	public void addUserPrint(String userId, String userPrint) {
		users.get(userId).addUserPrint(userPrint);
	}

	/**
	 * Add user log in to a given user (username and password)
	 * @param userId
	 * @param username
	 * @param password
	 * @throws NoSuchAlgorithmException passwords are hashed, if the hashing does not work this exception is thrown
	 */
	public void addUserLogin(String userId, String username, String password) throws NoSuchAlgorithmException {
		users.get(userId).addUserLogin(username, password);
	}

	/**
	 * Add an entitlement to a user (permission, role, resource role)
	 * @param userId user to get the entitlement
	 * @param entitlement to be added
	 */
	public void addUserEntitlement(String userId, String entitlement) {
		users.get(userId).addEntitlement(entitlements.get(entitlement));
	}

	/**
	 * Add an entitlement to a role (or resource role)
	 * @param role to be added to
	 * @param entitlement to be added
	 */
	public void addRoleEntitlement(String role, String entitlement) {
		entitlements.get(role).addEntitlement(entitlements.get(entitlement));
	}

	/**
	 * Authenticate the passed user print for the user. Return an authentication
	 * token if the print is successfully authorized. Raise exceptions if not.
	 * @param userId user to be authenticated
	 * @param userPrint bio authentication provided
	 * @return auth token
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	public String authenticateUserPrint(String userId, String userPrint)
			throws AccessDeniedException, InvalidAuthTokenException {
		return users.get(userId).authenticatePrint(userPrint);
	}

	/**
	 * Log a user in by providing username and password. Get an auth token in return if the login is
	 * successful
	 * @param userId user to be logged in
	 * @param username
	 * @param password
	 * @return authentication token
	 * @throws InvalidAuthTokenException
	 * @throws NoSuchAlgorithmException if password hashing fails
	 * @throws AccessDeniedException
	 */
	public String userLogin(String userId, String username, String password) throws InvalidAuthTokenException, NoSuchAlgorithmException, AccessDeniedException {
		return users.get(userId).login(username, password);
	}

	/**
	 * Invalidate the user's authentication token
	 * @param userId user to be logged out
	 */
	public void userLogout(String userId) {
		users.get(userId).logout();
	}

	/**
	 * Show the details of the auth service as it currently is. Uses the inventory visitor.
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	public void show() throws AccessDeniedException, InvalidAuthTokenException {
		InventoryVisitor visitor = new InventoryVisitor();
		acceptVisitor(visitor);
		System.out.println(visitor.getInventory());
	}

	/**
	 * Check whether the user with the passed auth token has the permission on the passed resource
	 * Uses the permission visitor
	 * @param authToken auth token for user attempting restricted action
	 * @param permission permission required for restricted action
	 * @param resource resource on which the action is taking place (can be none)
	 * @return true or false if the user has the permission
	 * @throws AccessDeniedException if user does not have permission
	 * @throws InvalidAuthTokenException if auth token is invalid
	 */
	public boolean checkPermission(String authToken, String permission, String resource) throws AccessDeniedException, InvalidAuthTokenException {
		PermissionVisitor visitor = new PermissionVisitor(permission, resource, authToken);
		acceptVisitor(visitor);
		// if the visitor did not find the auth token, the token must be invalid
		if (!visitor.getFoundAuthToken()) {
			throw new InvalidAuthTokenException("checkPermission", "auth token not found");
		} else {
			// if the visitor found the auth token but it does not have the permission, the user does not have that
			// permission
			if (!visitor.getAuthTokenHasPermission()) {
				throw new AccessDeniedException("checkPermission", "auth token does not have permission: " + permission);
			} else {
				return true;
			}
		}
	}

	/**
	 * Private method to accept a visitor of the authentication service
	 * @param visitor visitor interface
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	private void acceptVisitor(Visitor visitor) throws AccessDeniedException, InvalidAuthTokenException {
		visitor.visitAuthenticationService(this);
	}

	/**
	 * Getter for the list of users known to the auth service
	 * @return collection of users
	 */
	Collection<User> getUsers(){
		return users.values();
	}

	/**
	 * Getter for resources
	 * @return collection of resources
	 */
	Collection<Resource> getResources(){
		return resources.values();
	}

	/**
	 * Getter for entitlements
	 * @return collection of entitlements
	 */
	Collection<Entitlement> getEntitlements(){
		return entitlements.values();
	}
}
