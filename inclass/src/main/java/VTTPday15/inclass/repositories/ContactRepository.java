package VTTPday15.inclass.repositories;

import java.util.HashMap;
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

import VTTPday15.inclass.model.Contact;

//used to interact with the database via Create/Read/Update/Delete operations
//basically data access layer
//RedisTemplate is high-level abstraction, allows us to interact with Redis in a generic way
//HashOperations is a specialized interface, allows us to interact with Redis "hashes" e.g. key value pairs
    //HashOperations<String, String, Object> hashOps = template.opsForHash();
    // hashOps.put("contact:1", "name", "Sam");  -> put key:name and value:Sam in the hash "key" (contact:1)
    // hashOps.put("contact:1", "email", "sam@gmail.com");


@Repository
public class ContactRepository {

    //Dependency inject the redis template into ContactRepository
    //@Qualifier specifies the specific AppConfig bean to inject if there are multiple RedisTemplate beans defined in AppConfig
    @Autowired @Qualifier("redis-object")
    private RedisTemplate<String, Object> template;
    //RedisTemplate<String, Object>
        //String represents the Redis key 'type'
        //Object represents the Redis value 'type'

    //retrieve contact by ID from redis db
    //keys *(wildcard) -> returns a Set of all keys in the type of String
    public Set<String> getContactIds(){
        return template.keys("*");
    }


    //hgetall {id} -> returns the specific id's other params with messed up prefix
    public Optional<Contact> getContactById(String id){
        //HashOperations provides methods to interact with Redis hashes
        HashOperations<String, String, Object> hashOps = template.opsForHash(); 
        //retrieves all keys(name/email/phone) and values(sam/sam@gmail/999) of the Redis hash assocaited with the specific UUID
        //UUID being the map's name
        //hgetall {id}
        Map<String, Object> contact = hashOps.entries(id); 

        if(contact.isEmpty()){
            return Optional.empty(); //if Map<String, Object> is empty, return empty Contact object
        }

        //else, return a Contact object with only name/email/phone/if set. Note that id is set in Contact service
        Contact results = new Contact();
        results.setName(contact.get("name").toString());
        results.setEmail(contact.get("email").toString());
        results.setPhone(contact.get("phone").toString());

        return Optional.of(results); //Contact object

    }


    //hset abc123 name fred
    //hset abc123 email fred@gmail.com
    public void insertContact(Contact contact){

        //ListOperations<String, Object> listOps = template.opsForList();
        //ValueOperations<String, Object> vauleOps = template.opsForValue();

       //keyserializer(map name), hashkey, value
       HashOperations<String, String, Object> hashOps = template.opsForHash();

        //put sequentially
            // hashOps.put(contact.getId(), "name", contact.getName());
            // hashOps.put(contact.getId(), "email", contact.getEmail());
            // hashOps.put(contact.getId(), "phone", contact.getPhone());
       
        //batch put (setting the contact.get{variable} values for a map)
        Map<String, Object> values = new HashMap();
        values.put("name", contact.getName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());

        hashOps.putAll(contact.getId(), values);   //putting name/email/phone into redis with id as the map's name
    }
    




    
}
