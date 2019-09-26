import java.util.ArrayList;
import java.util.HashMap;

public class Store {
	private String id;
	private String address;
	private String name;
	private int numberCurrentCustomers;
	private HashMap<String, Aisle> aisleMap;
	private HashMap<String, ArrayList<Inventory>> inventoryMap;
	private HashMap<String, Device> deviceMap;

	public Store(String id, String address, String name) {
		this.id = id;
		this.address = address;
		this.name = name;
		numberCurrentCustomers = 0;
		aisleMap = new HashMap<String, Aisle>();
		deviceMap = new HashMap<String, Device>();
		inventoryMap = new HashMap<String, ArrayList<Inventory>>();
	}

	public void addAisle(Aisle aisle) {
		aisleMap.put(aisle.getNumber(), aisle);
	}

	public void addDevice(Device device) {
		//deviceMap.put(device.getId(), device);
	}

	public void addInventory(Inventory inventory) {
		if (inventoryMap.containsKey(inventory.getProductId())) {
			inventoryMap.get(inventory.getProductId()).add(inventory);
		} else {
			ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
			inventoryList.add(inventory);
			inventoryMap.put(inventory.getProductId(), inventoryList);
		}
	}
}
