import java.util.ArrayList;
import java.util.Arrays;

public class Product {
	private String id;
	private String name;
	private String description;
	private int size;
	private String category;
	private int price;
	private String temperature;
	private ArrayList<String> temperatures;

	public Product(String id, String name, String description, int size, String category, int price, String temperature){
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
		temperatures = new ArrayList<String>(Arrays.asList("Frozen", "Refrigerated", "Ambient", "Warm", "Hot"));

		if (temperatures.contains(temperature)){
			this.temperature = temperature;
		}
	}

	public Product(String id, String name, String description, int size, String category, int price){
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
		this.temperature = "Ambient";
	}

	public String getId(){
		return id;
	}

	public String getTemperature(){
		return temperature;
	}

}
