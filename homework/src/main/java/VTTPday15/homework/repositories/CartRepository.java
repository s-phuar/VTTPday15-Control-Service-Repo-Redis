package VTTPday15.homework.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import VTTPday15.homework.models.Cart;
import VTTPday15.homework.models.User;

@Repository
public class CartRepository {
    @Autowired @Qualifier("redis-object")
    private RedisTemplate<String, Object> template;

    //keys *
    //get all userIds
    public Set<String> getAllUserIds(){
        return template.keys("*"); //should return sam@gmail.com/fred@gmail.com etc.
    }

    //hkeys {userId}
    public Set<String> getAllCartIds(String userId){
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        return hashOps.keys(userId); //return all field keys (cartId) for a specific userId
    }

    //return specific cart object holding Set of Maps(K:V)
    public Cart getSpecCart (String userId, String cartId){
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        return (Cart) hashOps.get(userId, cartId); //should return cart object (BUT it doeeeeeeeeeeenst)
    }

    public Map<String, Object> getCartMap(String userId){
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        return (Map<String, Object>) hashOps.entries(userId);
    }



    //HSET {userId} {""} {""}
    //HDEL {userId} {""}
    public void saveNewUser(User user){ //new user with no cart
        HashOperations<String, String, Object> hashOps = template.opsForHash(); 

        hashOps.put(user.getUserId(), "", ""); 
        hashOps.delete(user.getUserId(), ""); //just delete the ""(cartId) cart
    }

    //used to create/enter userId, create/enter cart, create/modify, grocery
        //if userId does not exists, we create a new userId
        //if userId exists but cart does not exists, the cart is created
        //if both exists, that specific cart is overwritten entirely. Other carts not affected

                // //HSET{userId} {cartId} {""}
                // public void saveNewCart(String userId, String cartId){
                //     HashOperations<String, String, Object> hashOps = template.opsForHash();
                
                //     hashOps.put(userId, cartId, ""); //empty string as value
                // }

    //HSET {userId} {cartId} {items}
    //can be used to create new user's cart, or update existing cart
    public void saveNewCart(String userId, String cartId, Cart cart){
        //keyserializer(map name), hashkey, value
        // System.out.printf("%s, %s, %s, %d\n\n\n\n\n\n\n", userId, cartId, cart.getCartId(), cart.getTotalQuantity());
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        
        hashOps.put(userId, cartId, cart);
    }






}
