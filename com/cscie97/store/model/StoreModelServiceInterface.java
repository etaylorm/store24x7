public interface StoreModelServiceInterface {
	public void addBasketItem(String basketId, String productId, int itemCount);
	public void removeBasketItem(String basketId, String productId, int itemCount);
	public void clearBasket(String basketId);
	public void showBasketItems();
	public void updateInventory(String inventoryId, int incrementCount);
	public void updateCustomer(String customerId, String location);
	public void createEvent(String deviceId, String event);
	public void createCommand(String deviceId, String command);
}
