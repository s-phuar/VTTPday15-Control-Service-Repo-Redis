package VTTPday15.workshop.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPday15.workshop.models.Cart;
import VTTPday15.workshop.models.Grocery;
import VTTPday15.workshop.models.User;
import VTTPday15.workshop.repositories.CartRepository;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepo;

    public Set<String> getUsers(){
        return cartRepo.getAllUsers(); //return the users
    }

    public Set<String> getCarts(String userId){
        return cartRepo.getAllCarts(userId); //return a Set<String> to ContactController
    }

    public List<Grocery> getCartItems(String userId, String cartId){
        return (List<Grocery>) cartRepo.getAllCartItems(userId, cartId);
    }

    public String createUser(String userId){
        User user = new User();
        user.setId(userId);
        cartRepo.saveNewUser(user);
        cartRepo.deleteCart(user, ""); //delete empty cartId for new user
        return user.getId(); 
    }

    public String createCart(String userId){
        String cartId = UUID.randomUUID().toString().substring(0,8); //generate a 8 digit UUID string

        cartRepo.saveNewCart(userId, cartId);

        return cartId;
    }


    public void updateCart(String userId, String cartId, String item, int quantity){
        //will be used in add and modify controllers as it returns a List regardless if it is empty
        List<Grocery> groceries = cartRepo.getAllCartItems(userId, cartId); //this groceryList should be kept up to date at all times

        Grocery grocery = new Grocery();
        grocery.setItem(item);
        grocery.setQuantity(quantity);

        groceries.add(grocery);


        cartRepo.updateExistingCart(userId, cartId, groceries);

    }

    public void saveCart(String userId, String cartId) {
        cartRepo.saveNewCart(userId, cartId);  // Saves the cart (e.g., to the database)
    }




}
