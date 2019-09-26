import java.util.HashMap;

public class Basket {
	private String id;
	private HashMap<String, Integer> productCount;

	public Basket(String id){
		this.id = id;
		productCount = new HashMap<String, Integer>();
	}

	public HashMap<String,Integer> getBasketItems() {
		return productCount;
	}

	public void addProduct(String productId, int count){
		productCount.merge(productId, count, Integer::sum);
	}

	public void removeProduct(String productId, int count){
		if (productCount.get(productId) >= count) {
			productCount.put(productId, productCount.get(productId) - count);
		}
	}

	public void clearBasket() {
		productCount = new HashMap<String, Integer>();
	}

	public String getId(){
		return id;
	}
}
