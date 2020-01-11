package com.cscie97.store.model;

/**
 * Turnstiles are a type of appliance that allows customers in and out of the store.
 */
public class Turnstile extends Appliance {
	private boolean open;

	Turnstile(String id, String name, String type, Location location) {
		super(id, name, type, location);
		open = false;
	}

	/**
	 * Complete task assigned to the turnstile
	 * @param command either standard device command or a special turnstile command which will change
	 *                the state of the turnstile to open or closed
	 */
	public void issueCommand(String command) {
		super.issueCommand(command);
		if (command.startsWith("open")){
			open = true;
			System.out.println("--" + id + " opened");
		} else if (command.startsWith("close")){
			open = false;
			System.out.println("--" + id + " closed");
		}
	}

	/**
	 * Display information about the turnstile
	 * @return string of information
	 */
	public String show(){
		String info = super.show();
		return info +
				"\n\topen: " + open;
	}
}
