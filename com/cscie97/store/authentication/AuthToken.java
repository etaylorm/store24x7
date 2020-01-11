package com.cscie97.store.authentication;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Random;

/**
 * Authentication token that is used to verify users and their permissions within the
 * authentication service
 */
class AuthToken {
	private String value;
	private LocalTime lastUsed;
	private LocalTime expirationDate;

	AuthToken(){
		value = generateAuthToken();
		lastUsed = null;
		expirationDate = LocalTime.now().plusMinutes(10);
	}

	/**
	 * In special cases (admin) allow the auth token's value to be specified
	 * @param value
	 */
	AuthToken(String value){
		this.value = value;
		lastUsed = null;
		expirationDate = LocalTime.now().plusMinutes(10);
	}

	/**
	 * Getter for the value of the authentication token
	 * - If the token is not expired, return it and update the last used timestamp
	 * @return auth token
	 * @throws InvalidAuthTokenException if the token is expired
	 */
	String getValue() throws InvalidAuthTokenException{
		if (!isExpired()) {
			lastUsed = LocalTime.now();
			return value;
		}
		else {
			throw new InvalidAuthTokenException("getAuthToken", "authToken is expired");
		}
	}

	/**
	 * Helper method to determine if the auth token has expired
	 * @return
	 */
	private boolean isExpired() {
		return LocalTime.now().compareTo(expirationDate) > 0;
	}

	/**
	 * Generate an authentication token when the auth token is created by
	 * randomly choosing characters
	 * @return auth token
	 */
	private String generateAuthToken(){
		byte[] array = new byte[10];
		new Random().nextBytes(array);

		return new String(array, StandardCharsets.UTF_8);
	}

	@Override
	public String toString(){
		return "\tvalue: " + value +
				"\n\tlast used: " + lastUsed +
				"\n\texpiration date: " + expirationDate;
	}
}
