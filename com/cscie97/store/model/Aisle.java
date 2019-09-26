import java.util.HashMap;

public class Aisle {
	private String number;
	private String name;
	private String description;
	private String location;
	private HashMap<String, Shelf> shelfMap;

	public Aisle(String number, String name, String description,  String location){
		this.number = number;
		this.name = name;
		this.description = description;
		this.location = location;
		shelfMap = new HashMap<String, Shelf>();
	}

	public void addShelf(Shelf shelf){
		shelfMap.put(shelf.getId(), shelf);
	}

	public void removeShelf(String shelfId){
		shelfMap.remove(shelfId);
	}

	public String getNumber(){
		return number;
	}
}
