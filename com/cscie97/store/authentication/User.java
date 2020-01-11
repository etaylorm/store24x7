package com.cscie97.store.authentication;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Representation of a user within the authentication service. Users can be authenticated using username and password
 * or by bio prints (face or voice). Users, once authenticated, have temporary auth tokens.
 */
public class User {
	private String id;
	private String name;
	private Login login;
	private ArrayList<Entitlement> entitlements;
	private AuthToken authToken;
	private ArrayList<BioPrint> prints;

	User(String id, String name){
		this.id = id;
		this.name = name;

		authToken = null;
		login = null;
		entitlements = new ArrayList<Entitlement>();
		prints = new ArrayList<BioPrint>();
	}

	/**
	 * Add a username and password as a credential for the user
	 * @param username
	 * @param password
	 * @throws NoSuchAlgorithmException
	 */
	void addUserLogin(String username, String password) throws NoSuchAlgorithmException {
		login = new Login(username, password);
	}

	/**
	 * Add a user bio print as a credential for the user
	 * @param print bio print
	 */
	void addUserPrint(String print) {
		prints.add(new BioPrint(id, print));
	}

	/**
	 * Authenticates the user by logging them in. If the log in is successful, an auth token is returned.
	 * @param username credential
	 * @param password credential
	 * @return auth token
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAuthTokenException
	 * @throws AccessDeniedException
	 */
	String login(String username, String password)
			throws NoSuchAlgorithmException, InvalidAuthTokenException, AccessDeniedException {
		// if the username is admin, set the auth token equal to the password
		if (username.equals("admin")){
			authToken = new AuthToken(password);
			return authToken.getValue();
		} else {
			if (login.loginSuccessful(username, password)) {
				// return or generate new auth token
				return returnAuthToken().getValue();
			} else {
				throw new AccessDeniedException("login", "invalid login");
			}
		}
	}

	/**
	 * Returns an auth token and generates a new auth token if it doesn't exist
	 * @return auth token
	 */
	private AuthToken returnAuthToken() {
		if (authToken == null){
			authToken = new AuthToken();;
		}
		return authToken;
	}

	/**
	 * Return the value of the user's auth token if it exists, otherwise return null
	 * @return auth token or null
	 * @throws InvalidAuthTokenException
	 */
	String getAuthToken() throws InvalidAuthTokenException {
		if (authToken != null) {
			return authToken.getValue();
		}
		return null;
	}

	/**
	 * Authenticate a user print (face of voice). If the print matches any known print for the user,
	 * return the an authentication token for the user
	 * @param value print
	 * @return auth token
	 * @throws InvalidAuthTokenException
	 * @throws AccessDeniedException
	 */
	String authenticatePrint(String value)
			throws InvalidAuthTokenException, AccessDeniedException {
		for (BioPrint print : prints){
			if (print.printMatches(value)){
				return returnAuthToken().getValue();
			}
		}
		throw new AccessDeniedException("authenticatePrint", "invalid print");
	}

	/**
	 * Log the user out, which invalidates their auth token
	 */
	void logout() {
		authToken = null;
	}

	/**
	 * Adds entitlement to the user's list of entitlements
	 * @param entitlement to be added
	 */
	void addEntitlement(Entitlement entitlement) {
		entitlements.add(entitlement);
	}

	/**
	 * Accepts inventory and permission visitors
	 * @param visitor
	 * @throws AccessDeniedException
	 * @throws InvalidAuthTokenException
	 */
	public void acceptVisitor(Visitor visitor) throws AccessDeniedException, InvalidAuthTokenException {
		visitor.visitUser(this);
	}

	/**
	 * Returns a string builder of information about the user
	 * @return stringbuilder
	 */
	public StringBuilder show() {
		StringBuilder info = new StringBuilder("User:" +
				"\n\tid: " + id +
				"\n\tname: " + name);

		if (login != null){
			info.append("\n\tlogin: ").append(login.toString());
		}
		if (authToken != null) {
			info.append("\n\tauth token:\n\t").append(authToken.toString());
		}

		info.append("\n\tentitlements:");
		for (Entitlement entitlement : entitlements){
			info.append("\n\t");
			info.append(entitlement.show());
		}
		info.append("\n\tprints:");
		for (BioPrint print : prints){
			info.append("\n\t");
			info.append(print.toString());
		}
		return info;
	}

	/**
	 * Method to determine whether this user has the specified permission on the given resource
	 * @param permission to look for
	 * @param resource that the permission is required for
	 * @return true if yes, false if not
	 */
	boolean hasPermission(String permission, String resource) {
		for (Entitlement entitlement : entitlements) {
			if (entitlement.hasPermission(permission, resource)) {
				return true;
			}
		}
		return false;
	}
}
