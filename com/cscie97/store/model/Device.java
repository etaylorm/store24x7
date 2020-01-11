package com.cscie97.store.model;

/**
 * A class of objects within the stores that sense events taking place
 * within the store. Includes microphones, cameras, speakers, robots, and turnstiles.
 */
class Device {
	protected Location location;
	protected String id;
	protected String name;
	protected String type;

	Device(String id, String name, String type, Location location){
		this.id = id;
		this.name = name;
		this.type = type;
		this.location = location;
	}

	/**
	 * Getter for the device's location
	 * @return location (specified by store and aisle)
	 */
	Location getLocation(){
		return location;
	}

	/**
	 * Getter for the type of device (microphone, speaker, turnstile, robot, camera)
	 * @return type of device
	 */
	String getType(){
		return type;
	}

	/**
	 * Sensors can report events occurring within the store
	 * @param event
	 */
	void reportEvent(String event){
		// method that would be invoked if not all events within the system are simulated
	}

	/**
	 * Getter for the device's id
	 * @return id unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns informational string about the device
	 * @return string details
	 */
	String show(){
		return "\n\tid: " + id +
				"\n\tname: " +  name +
				"\n\ttype: " + type +
				"\n\tlocation: " + location +
				"\n";
	}

	public void issueCommand(String command) {
	}

	public void updateLocation(Location location) {
	}
}
