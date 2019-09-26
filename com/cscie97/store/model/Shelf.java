import java.util.HashMap;

public class Shelf {
	private String id;
	private String name;
	private String level;
	private String description;
	private String temperature;
	private HashMap<String, Product> productMap;

	public Shelf(String id, String name, String level, String description, String temperature){
		this.id = id;
		this.name = name;
		this.level = level;
		this.description = description;
		this.temperature = temperature;
		productMap = new HashMap<String,Product>();
	}

	public void addProduct(Product product) {
		if (product.getTemperature().equals(temperature)){
			productMap.put(product.getId(), product);
		}
	}

	public Product removeProduct(String productId){
		return productMap.remove(productId);
	}

	public String getId(){
		return id;
	}
}
