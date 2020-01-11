package com.cscie97.store.authentication;

import java.security.NoSuchAlgorithmException;

/**
 * Interface for the Authentication Service.
 */
public interface AuthenticationServiceInterface {
	void definePermission(String id, String name, String description);
	void defineResource(String id, String description);
	void defineResourceRole(String id, String name, String description, String role, String resource);
	void defineRole(String id, String name, String description);
	void createUser(String id, String name);
	void addUserPrint(String userId, String userPrint);
	void addUserLogin(String userId, String username, String password) throws NoSuchAlgorithmException;
	void addUserEntitlement(String userId, String entitlement);
	void addRoleEntitlement(String role, String entitlement);
	boolean checkPermission(String authToken, String permission, String resource) throws AccessDeniedException, InvalidAuthTokenException;
	String authenticateUserPrint(String userId, String userPrint) throws AccessDeniedException, InvalidAuthTokenException;
	String userLogin(String userId, String username, String password) throws InvalidAuthTokenException, NoSuchAlgorithmException, AccessDeniedException;
	void userLogout(String userId);
	void show() throws AccessDeniedException, InvalidAuthTokenException;
}
