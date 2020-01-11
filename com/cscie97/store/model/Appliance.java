package com.cscie97.store.model;

/**
 * An appliance is a type of device that can execute commands
 */
public class Appliance extends Device {
    Appliance(String id, String name, String type, Location location) {
        super(id, name, type, location);
    }

    /**
     * Certain types of devices can also complete tasks
     * @param command prints out success message that the appliance recieved the command (and presumably
     *                knows how to execute it)
     */
    public void issueCommand(String command) {
        System.out.println("-- device " + id + " of type " + type + " completed command: " + command);
    }
}
