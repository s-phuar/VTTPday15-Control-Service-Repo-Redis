package VTTPday15.homework.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import VTTPday15.homework.AppConfig;
import VTTPday15.homework.models.Cart;
import VTTPday15.homework.models.User;
import VTTPday15.homework.services.CartService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.swing.plaf.multi.MultiListUI;

@Controller
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartSvc;

    @GetMapping("/")
    public String landingPage(){
        return "index";
    }

    //when we submit landing page, we are using POST
    @PostMapping("/carts")
    public String getCartsID(@RequestParam("userId") String userId, HttpSession sess, Model model){

        if(!cartSvc.getAllUsers().contains(userId)){
            //create new user
            cartSvc.createUser(userId);
        }

        sess.setAttribute("userId", userId);

        model.addAttribute("userId", userId);
        model.addAttribute("Carts", cartSvc.getAllCartMaps(userId)); //sends over the Map<cartId, cart Object>

        return "cartList";
    }


    // View details of a specific cart (display existing groceries)
    @PostMapping("/carts/create")
    public String createCart(HttpSession sess, Model model){
        String userId = (String) sess.getAttribute("userId");
        String cartId = cartSvc.createCart(userId); //create cart and save uuid

        return "redirect:/carts/" + cartId;
    }

    @GetMapping("/carts/{cartId}")
    public String cartModify(@PathVariable String cartId, HttpSession sess, Model model){

        String userId = (String) sess.getAttribute("userId");
        Cart cart = cartSvc.getAllCartItems(userId, cartId); // specific cartid's cart object

        model.addAttribute("userId", userId);
        model.addAttribute("cartId", cartId);
        model.addAttribute("cartMap", cart.getItem());//display the cart object's <String, Integer> item map

        return "cartModify";
    }

    @PostMapping("/carts/{cartId}/add")
    public String addItem(@RequestBody MultiValueMap<String, String> form,
    @PathVariable String cartId, HttpSession sess, Model model){

        String userId = (String) sess.getAttribute("userId");

        //update Cart object
        String item = form.getFirst("item");
        int quantity = Integer.parseInt(form.getFirst("quantity"));

        cartSvc.updateCart(userId, cartId, item, quantity); // update cart object
        Cart cart = cartSvc.getAllCartItems(userId, cartId); // grab specific cartid's cart object


        model.addAttribute("userId", userId);
        model.addAttribute("cartId", cartId);
        model.addAttribute("cartMap", cart.getItem());//display the cart object's <String, Integer> item map

        // System.out.printf("user ID: %s\n\n\n\n\n\n\n",userId); //this picks up the cartId from url (debugging)
        // System.out.printf("cart ID: %s\n\n\n\n\n\n\n",cartId); //this picks up the cartId from url (debugging)
        // System.out.printf("cart Item: %s\n\n\n\n\n\n\n",cart.getItem()); //this picks up the cartId from url (debugging)

        return "cartModify";
    }

    @GetMapping("/carts/save")
    public String saveCart(HttpSession sess, Model model) {

        String userId = (String)sess.getAttribute("userId");

        model.addAttribute("userId", userId);
        model.addAttribute("Carts", cartSvc.getAllCartMaps(userId)); //sends over the Map<cartId, cart Object>

        return "cartList";

        // return "redirect:/carts"; // Redirect to the cart list view
    }

}
