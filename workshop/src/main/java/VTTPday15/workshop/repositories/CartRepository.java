package VTTPday15.workshop.repositories;

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

import VTTPday15.workshop.models.Grocery;
import VTTPday15.workshop.models.User;

@Repository
public class CartRepository {
    @Autowired @Qualifier("redis-object")
    private RedisTemplate<String, Object> template;

    //keys *
    //get all userIds
    public Set<String> getAllUsers(){
        return template.keys("*"); //return all the users that exists
    }

    //hkeys {userId}
    //get all cartIds associated with a specific userId
    public Set<String> getAllCarts(String userId){
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        return hashOps.keys(userId); //return just the field key (cartId) for a specific userId
    }

    //get the groceries associated with each cart
    public List<Grocery> getAllCartItems(String userId, String cartId){
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        return (List<Grocery>) hashOps.get(userId, cartId);
    }

    public void deleteCart(User user, String cartId){
        HashOperations<String, String, Object> hashOps = template.opsForHash(); 

        hashOps.delete(user.getId(), cartId);
    }


    //save new user with EMPTY cart and EMPTY grocery list
    public void saveNewUser(User user){
        HashOperations<String, String, Object> hashOps = template.opsForHash(); 

        //empty cart string
        String cartId = "";

        //empty grocery list
        List<Grocery> groceries = new ArrayList<>();

        hashOps.put(user.getId(), cartId, groceries); 
    }

    //used to create/enter userId, create/enter cart, create/modify, grocery
        //if userId does not exists, we create a new userId
        //if userId exists but cart does not exists, the cart is created
        //if both exists, that specific cart is overwritten entirely. Other carts not affected
    public void saveNewCart(String user, String cartId){

        //keyserializer(map name), hashkey, value
        HashOperations<String, String, Object> hashOps = template.opsForHash();

        //empty grocery list
        List<Grocery> groceries = new ArrayList<>();
    
        hashOps.put(user, cartId, groceries);
    }


    public void updateExistingCart(String user, String cartId, List<Grocery> groceries){

        //keyserializer(map name), hashkey, value
        HashOperations<String, String, Object> hashOps = template.opsForHash();
    
        hashOps.put(user, cartId, groceries);
    }




}
