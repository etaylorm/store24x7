package com.cscie97.store.authentication;

/**
 * Visitor interface specifying what types of objects are visited within the authentication service
 */
public interface Visitor {
	void visitUser(User user) throws AccessDeniedException, InvalidAuthTokenException;
	void visitResource(Resource resource);
	void visitEntitlement(Entitlement entitlement);
	void visitAuthenticationService(AuthenticationService authenticationService) throws AccessDeniedException, InvalidAuthTokenException;
}
