public class Inventory {
	private String id;
	private String location;
	private String productId;
	private int count;
	private int capacity;

	public Inventory(String id, String location, String productId, int count, int capacity){
		this.id = id;
		this.location = location;
		this.productId = productId;
		this.count = count;
		this.capacity = capacity;
	}

	public boolean atCapacity() {
		return (count == capacity);
	}

	public void incrementInventory(){
		if (count < capacity){
			count ++;
		}
	}

	public void decrementInventory(){
		if (count > 0){
			count --;
		}
	}

	public String getProductId(){
		return productId;
	}

}
