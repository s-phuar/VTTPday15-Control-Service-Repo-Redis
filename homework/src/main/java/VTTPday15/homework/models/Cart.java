package VTTPday15.homework.models;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private String cartId;
    private Map<String, Integer> item = new HashMap<>();
    private int totalQuantity = 0;

    public int getTotalQuantity() {return totalQuantity;}
    public void setTotalQuantity(int totalQuantity) {this.totalQuantity = totalQuantity;}

    public String getCartId() {return cartId;}
    public void setCartId(String cartId) {this.cartId = cartId;}

    public Map<String, Integer> getItem() {return item;}
    public void setItem(Map<String, Integer> item) {this.item = item;}
    
    //getordefault
    //if key exists in the map, returns the value associated with the key
    //if key does NOT exist in the map, returns the default value I provided
    public void addItems(String itemName, int quantity){
        item.put(itemName, item.getOrDefault(itemName, 0)+ quantity);
        totalQuantity ++;
    }

    // public void validate() {
    //     if (totalQuantity == 0 && item != null) {
    //         totalQuantity = item.values().stream().mapToInt(Integer::intValue).sum();
    //     }
    // }


    @Override
    public String toString() {
        return "Cart ID: " + cartId + ", Items: " + item + ", Total Quantity: " + totalQuantity;
    }


}
