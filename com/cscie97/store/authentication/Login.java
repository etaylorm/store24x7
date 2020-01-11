package com.cscie97.store.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * User log in credentials that can be used to generate an authentication token
 * within the authentication service
 */
class Login {
	private String username;
	private String passwordHash;

	Login(String username, String password) throws NoSuchAlgorithmException {
		this.username = username;
		this.passwordHash = hashPassword(password);
	}

	/**
	 * Helper method to hash the users password for storing
	 * @param password to be hashed
	 * @return hashed version of the password
	 * @throws NoSuchAlgorithmException
	 */
	private String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash);
	}

	/**
	 * Checks whether the providing login in credentials match with the existing credentials
	 * @param username passed
	 * @param password passed
	 * @return true or false
	 * @throws NoSuchAlgorithmException
	 */
	boolean loginSuccessful(String username, String password) throws NoSuchAlgorithmException {
		return (this.username.equals(username) & hashPassword(password).equals(this.passwordHash));
	}
}
