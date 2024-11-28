package VTTPday15.homework.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import VTTPday15.homework.models.Cart;
import VTTPday15.homework.models.User;
import VTTPday15.homework.repositories.CartRepository;

@Service
public class CartService {
    
    @Autowired CartRepository cartRepo;

    public Set<String> getAllUsers(){
        return cartRepo.getAllUserIds();
    }

    public Set<String> getAllCarts(String userId){
        return cartRepo.getAllCartIds(userId); //return a Set<String> to cart controller
    }

    public Cart getAllCartItems(String userId, String cartId){
        return (Cart)cartRepo.getSpecCart(userId, cartId);
    }

    public Map<String, Object> getAllCartMaps(String userId){
        return (Map<String, Object>) cartRepo.getCartMap(userId);
    }

    public String createUser(String userId){
        User user = new User();
        user.setUserId(userId);
        cartRepo.saveNewUser(user);
        return user.getUserId(); //returns the user's unique email
    }

    public String createCart(String userId){
        String cartId = UUID.randomUUID().toString().substring(0,8); //generate a 8 digit UUID string
        Cart cart = new Cart();
        cart.setCartId(cartId); //unique ID
        cart.setTotalQuantity(0);
        cart.setItem(new HashMap<String, Integer>());

        cartRepo.saveNewCart(userId, cartId, cart);
        return cartId;
    }

    public void updateCart(String userId, String cartId, String itemName, int quantity){
        Cart cart =  (Cart)cartRepo.getSpecCart(userId, cartId);

        cart.addItems(itemName, quantity);

        //use cartRepo to update the Cart object
        cartRepo.saveNewCart(userId, cartId, cart);
    }


}
