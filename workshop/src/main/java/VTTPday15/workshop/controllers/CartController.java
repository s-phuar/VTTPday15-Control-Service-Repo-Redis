package VTTPday15.workshop.controllers;

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

import VTTPday15.workshop.AppConfig;
import VTTPday15.workshop.models.Cart;
import VTTPday15.workshop.models.Grocery;
import VTTPday15.workshop.models.User;
import VTTPday15.workshop.services.CartService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;


@Controller
@RequestMapping
public class CartController {
    
    @Autowired
    private CartService cartSvc;

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }


    //User ID holds multiple cart objects
    //each cart object hold varying fruits and fruit qty
    @PostMapping("/carts")
    public String checkUsers(@RequestParam("userId") String userId, HttpSession sess, Model model){ 
    //form should only be grabbing userId from landing page

        //check whether User object exists inside db, for new id input
        if(!cartSvc.getUsers().contains(userId)){
            cartSvc.createUser(userId); //create new user if user does not exist
        }
        //validation later

        sess.setAttribute("userId", userId); //set userId in session 

        //retrieve all carts for this specific user, could be empty
        Set<String> allCarts = cartSvc.getCarts(userId); 


        model.addAttribute("userId", userId); //the guy that logged in
        model.addAttribute("cartIds", allCarts); //all of that guy's carts


        return "cart-nexus";
    }

    // View details of a specific cart (display existing groceries)
    @GetMapping("/carts/{cartId}")
    public String viewCart(@PathVariable String cartId, HttpSession sess, Model model) {
        String userId = (String) sess.getAttribute("userId");
        List<Grocery> groceries = cartSvc.getCartItems(userId, cartId); // Get groceries for the cart
        
        model.addAttribute("userId", userId);
        model.addAttribute("cartId", cartId);
        model.addAttribute("groceries", groceries);
        return "cart-details"; // View for cart details and modifying groceries
    }

    // Create a new cart for the user
    @PostMapping("/carts/create")
    public String createNewCart(HttpSession sess) {
        String userId = (String) sess.getAttribute("userId");
        String newCartId = cartSvc.createCart(userId); // Create new cart

        return "redirect:/carts/" + newCartId; // Redirect to newly created cart page, do not send to view.html
    }


    // Add a new grocery item to a cart
    @PostMapping("/carts/{cartId}/add")
    public String addGrocery(@PathVariable String cartId,
        @RequestParam("item") String item,
        @RequestParam("quantity") int quantity,
        HttpSession sess) {

        String userId = (String) sess.getAttribute("userId");

        if (userId == null) {
            // If no userId is found in session, redirect to login or an error page
            return "redirect:/";
        }
        // Validate if the cart belongs to the user
        if (!cartSvc.getCarts(userId).contains(cartId)) {
            // If the cart does not belong to the user, handle the error appropriately
            return "redirect:/carts";
        }

        try {
            // Attempt to update the cart
            cartSvc.updateCart(userId, cartId, item, quantity); // Update the cart with the new grocery item
        } catch (Exception e) {
            // Log the exception and show an error page or redirect back to the cart page
            e.printStackTrace();
            return "redirect:/carts/" + cartId; // Or show an error page
        }
        return "redirect:/carts/" + cartId; // Redirect back to cart details page to show updated groceries
    }

    // Save and return to the cart list view
    @PostMapping("/carts/{cartId}/save")
    public String saveCart(@PathVariable String cartId, HttpSession sess) {
        String userId = (String) sess.getAttribute("userId");
        cartSvc.saveCart(userId, cartId); // Save the cart
        return "redirect:/carts"; // Redirect to the cart list view
    }



}
