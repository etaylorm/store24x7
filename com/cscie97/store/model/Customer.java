import java.time.LocalTime;

public class Customer {
	private String id;
	private String firstName;
	private String lastName;
	private String type;
	private String email;
	private String accountId;
	private String location;
	public LocalTime lastSeen;
	private Basket basket;

	public Customer(String id, String firstName, String lastName, String type, String email, String accountId){
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.email = email;
		this.accountId = accountId;
	}

	public String getBasket(){
		if (basket == null){
			basket = new Basket("basket_" + id);
		}
		return basket.getId();
	}

	public void updateLocation(String location){
		this.location = location;
		lastSeen = LocalTime.now();
	}

//	public void addToBasket(String productId, int count) {
//		basket.addProduct(productId, count);
//	}
//
//	public void removeFromBasket(String productId, int count) {
//		basket.removeProduct(productId, count);
//	}
//
//	public HashMap<String, Integer> getBasketItems(){
//		return basket.getBasketItems()
//	}

}
