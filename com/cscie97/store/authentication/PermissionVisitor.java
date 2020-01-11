package com.cscie97.store.authentication;

/**
 * Visitor that checks whether an auth token has a given permission on a resource within the authentication service
 */
public class PermissionVisitor implements Visitor {
	private String permission;
	private String resource;
	private String authToken;
	private boolean foundAuthToken;
	private boolean authTokenHasPermission;

	PermissionVisitor(String permission, String resource, String authToken){
		this.permission = permission;
		this.resource = resource;
		this.authToken = authToken;
		foundAuthToken = false;
		authTokenHasPermission = false;
	}

	/**
	 * Visits each user within the authentication service until the auth token
	 * has been found (auth tokens are associated with one user only)
	 * @param authService
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	public void visitAuthenticationService(AuthenticationService authService) throws AccessDeniedException, InvalidAuthTokenException {
		for (User user : authService.getUsers()){
			if (!foundAuthToken){
				user.acceptVisitor(this);
			}
		}
	}

	/**
	 * 1. Checks whether the user's auth token matches the passed auth token and updates foundAuthToken if so
	 * 2. If so, checks whether the user has the required permission and updates authTokenHasPermission variable if so
	 * @param user
	 * @throws InvalidAuthTokenException
	 */
	public void visitUser(User user) throws InvalidAuthTokenException {
		if (authToken.equals(user.getAuthToken())) {
			foundAuthToken = true;
			if (user.hasPermission(permission, resource)) {
				authTokenHasPermission = true;
			}
		}
	}

	/**
	 * Does not visit resources
	 * @param resource
	 */
	public void visitResource(Resource resource) {

	}

	/**
	 * Does not visit entitlements
	 * @param entitlement
	 */
	public void visitEntitlement(Entitlement entitlement) {

	}

	/**
	 * Getter for the indicator foundAuthToken
	 * @return whether the auth token was found
	 */
	boolean getFoundAuthToken(){
		return foundAuthToken;
	}

	/**
	 * Getter for the indicator authTokenHasPermission
	 * @return whether the auth token has the permission
	 */
	boolean getAuthTokenHasPermission(){
		return authTokenHasPermission;
	}

}
