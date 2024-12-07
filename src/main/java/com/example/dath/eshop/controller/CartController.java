package com.example.dath.eshop.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dath.eshop.exception.CartLineItemException;
import com.example.dath.eshop.exception.ProductException;
import com.example.dath.eshop.exception.UserException;
import com.example.dath.eshop.model.Cart;
import com.example.dath.eshop.model.Product;
import com.example.dath.eshop.model.User;
import com.example.dath.eshop.security.ShopMeUserDetail;
import com.example.dath.eshop.service.CartService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String viewCart(
            @RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product cartProduct = this.cartService.getProductById(productId);
            Set<Product> listRelatedProducts = this.cartService.getRelatedProduct(cartProduct);
            model.addAttribute("pageTitle", "Cart ID |" + productId);
            model.addAttribute("cartProduct", cartProduct);
            model.addAttribute("listRelatedProducts", listRelatedProducts);
            return "cart/cart";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
            return "redirect:/main-page";
        }
    }

    @GetMapping("/cart/remove")
    public String removeCartItem(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam("cartLineItemId") Integer cartLineItemId,
            RedirectAttributes redirectAttributes) {
        try {
            User user = cartService.findUserById(customer.getUserId());
            cartService.deleteCartLineItem(cartLineItemId, user);
            redirectAttributes.addFlashAttribute(
                    "message", "Cart item with ID " + cartLineItemId + " removed successfully.");
        } catch (CartLineItemException | UserException ex) {
            redirectAttributes.addFlashAttribute("message", "Error: " + ex.getMessage());
        }
        return "redirect:/cart/shopping-cart";
    }

    @GetMapping("/cart/shopping-cart")
    public String viewShoppingCart(
            @AuthenticationPrincipal ShopMeUserDetail customer, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = cartService.findUserById(customer.getUserId());
            model.addAttribute("pageTitle", "Shopping Cart");
            Cart shoppingCart = cartService.getCartByCustomer(user);
            model.addAttribute("shoppingCart", shoppingCart);
            model.addAttribute("listCartLineItem", cartService.getListCartItemByCart(shoppingCart));
            return "/cart/shopping-cart";
        } catch (ProductException | UserException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
            return "redirect:/main-page";
        }
    }

    @PostMapping("/cart/update-cart")
    public String updateCart(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam(value = "productId") Integer productId,
            @RequestParam(value = "quantity") Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            if (productId == null || quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Invalid product ID or quantity.");
            }
            User customerUser = cartService.findUserById(customer.getUserId());
            Product selectedProduct = cartService.getProductById(productId);
            cartService.addProductToCart(customerUser, selectedProduct, quantity);
            redirectAttributes.addFlashAttribute("message", "Shopping cart updated successfully.");
            return "redirect:/cart?productId=" + productId;
        } catch (ProductException | IllegalArgumentException | UserException ex) {
            redirectAttributes.addFlashAttribute("errMessage", "Error: " + ex.getMessage());
            return "redirect:/cart?productId=" + productId;
        }
    }

    @GetMapping("/cart/clear")
    public String clearAllCartAndCartLineItems(
            @AuthenticationPrincipal ShopMeUserDetail customer, RedirectAttributes redirectAttributes) {
        try {
            User customerUser = cartService.findUserById(customer.getUserId());
            cartService.clearCard(customerUser);
            redirectAttributes.addFlashAttribute("message", "Cart cleared successfully.");
            return "redirect:/cart/shopping-cart";
        } catch (ProductException | UserException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
            return "redirect:/cart/shopping-cart";
        }
    }
}
