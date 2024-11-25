package VTTPday15.inclass.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPday15.inclass.model.Contact;
import VTTPday15.inclass.repositories.ContactRepository;

//implements the business logic of the application, acts as the intermediary between Controller and Repository

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepo;


    public Set<String> getContacts(){
        return contactRepo.getContactIds(); //return a Set<String> to ContactController
    }


    //public Contact getContatctById(String Id){
    public Optional<Contact> getContactById(String id){
        return contactRepo.getContactById(id); //return a Contact object to ContactController
    }


    public String insert(Contact contact){
        String id = UUID.randomUUID().toString().substring(0,8); //generate a 8 digit UUID string
        contact.setId(id); //sets ID of contact object whenever a new contact is added in the application

        contactRepo.insertContact(contact); //contact object that controller has just set name/email/phone, then set id here, is sent to redis to store

        return id; //return a String to ContactController
    }


}
