package VTTPday15.inclass.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import VTTPday15.inclass.AppConfig;
import VTTPday15.inclass.model.Contact;
import VTTPday15.inclass.services.ContactService;

import java.util.Optional;
import java.util.logging.Logger;


//form -> controller -> services via autowired DI -> repo via autowired DI
// Autowired -> Spring injects and instance of the dependency
//actually using the thing
    //mvn clean spring-boot:run
    //localhost:8080, input details
    //access redis via ubuntu & redis-cli
    //for checking purposes: keys *, to check what keys are in the redis db
    //hget 42777bc6 {fieldvariables}, data type of the key is a hash
    //hgetall 42777bc6, gives us all field variables at one go 
        //hkeys 42777bc6 works too


//Handles incoming HTTPrequests

@Controller
@RequestMapping
public class ContractController {

    private final Logger logger = Logger.getLogger(ContractController.class.getName());

    //Autowired is used to inject dependencies into the controller automatically
    //ContactService is injected to access the business logic layer that interacts with redis
    @Autowired
    private ContactService contactSvc;

    @GetMapping("/contacts")
    public String getContacts(Model model){

        model.addAttribute("ids", contactSvc.getContacts()); //Set<String> of all unique contacts is sent to "contact-list"

        return "contact-list";
    }


    @GetMapping("/contact/{id}")
    public ModelAndView getContactById(@PathVariable String id){

        Optional<Contact> opt = contactSvc.getContactById(id);
        ModelAndView mav = new ModelAndView();

        if(opt.isEmpty()){
            //404
            mav.setViewName("not-found");
            mav.setStatus(HttpStatusCode.valueOf(404));
            mav.addObject("id", id); //non-existent id is sent to view. It does not have associated name/email/phone
            return mav;
        }

        //else 
        Contact contact = opt.get();
        //200
        mav.setViewName("contact-info");
        mav.addObject("contact", contact); //Contact object of "contact" sent to view, note that object's id should also have been sent over
        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;

    }

    @PostMapping("/contact")
    public String postContact(@RequestBody MultiValueMap<String, String> form, Model model){
            String name = form.getFirst("name"); //grab name/email/phone from POST payload
            String email = form.getFirst("email");
            String phone = form.getFirst("phone");

            Contact contact = new Contact();    //set name/email/phone in new Contact object
            contact.setName(name);
            contact.setEmail(email);
            contact.setPhone(phone);

            logger.info("Adding %s to contact".formatted(name));
            //id is set in the service layer, while the other parameter are set above. id is set AFTER the other params
            //note that the method returns the String id while setting id at the same time
            String id = contactSvc.insert(contact); 

            model.addAttribute("name", name); //send over contact object with name/email/phone/id to view
            model.addAttribute("id", id); //send over id individually

        return "added";
    }



}
