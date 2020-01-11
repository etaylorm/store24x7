package com.cscie97.store.model;

/**
 * Robots are a type of appliance that complete specific tasks. As appliances, which are devices,
 * they are able to report events and respond to commands.
 */
public class Robot extends Appliance {
	private String lastTask;

	Robot(String id, String name, String type, Location location) {
		super(id, name, type, location);
	}

	/**
	 * Complete tasks supplied to them
	 * @param task
	 */
	public void issueCommand(String task){
		super.issueCommand(task);
		this.lastTask = task;
	}

	/**
	 * Update the robot's location
	 * @param location new location
	 */
	public void updateLocation(Location location){
		this.location = location;
		System.out.println("-- location of " + id + " updated to " + location);
	}

	/**
	 * Display information about the robot
	 * @return information string
	 */
	public String show(){
		String info = super.show();
		return info +
				"\n\tstate: " + lastTask +
				"\n\tcurrent location: " + location;
	}
}
