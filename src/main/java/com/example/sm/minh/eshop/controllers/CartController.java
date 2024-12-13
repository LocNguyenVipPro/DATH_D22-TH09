    package com.example.sm.minh.eshop.controllers;

    import com.example.sm.minh.eshop.exceptions.UserException;
    import com.example.sm.minh.eshop.models.Cart;
    import com.example.sm.minh.eshop.models.Product;
    import com.example.sm.minh.eshop.models.User;
    import com.example.sm.minh.eshop.exceptions.CartLineItemException;
    import com.example.sm.minh.eshop.exceptions.ProductException;
    import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
    import com.example.sm.minh.eshop.services.CartService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.Set;

    @Controller
    public class CartController {
        @Autowired
        private CartService cartService;

        @GetMapping("/cart")
        public String viewCart(@RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
            try {
                Product cartProduct = this.cartService.getProductById(productId);
                Set<Product> listRelatedProducts=this.cartService.getRelatedProduct(cartProduct);
                model.addAttribute("pageTitle", "Cart ID |" + productId);
                model.addAttribute("cartProduct", cartProduct);
                model.addAttribute("listRelatedProducts", listRelatedProducts);
                return "cart/cart";
            } catch (ProductException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
                return "redirect:/main-page";
            }
        }

        @GetMapping("/cart/remove")
        public String removeCartItem(@AuthenticationPrincipal ShopMeUserDetail customer,
                                     @RequestParam("cartLineItemId") Integer cartLineItemId,
                                     RedirectAttributes redirectAttributes) {
            try {
                User user = this.cartService.findUserById(customer.getUserId());
                this.cartService.deleteCartLineItem(cartLineItemId, user);
                redirectAttributes.addFlashAttribute("message", "Delete Cart With Id : "+cartLineItemId+" Successfully");
            } catch (CartLineItemException | UserException ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            }
            return "redirect:/cart/shopping-cart";
        }

        @GetMapping("/cart/shopping-cart")
        public String viewShoppingCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                       Model model, RedirectAttributes redirectAttributes) {
            try {
                User user = this.cartService.findUserById(customer.getUserId());

                model.addAttribute("pageTitle", "Cart");
                Cart shoppingCart = this.cartService.getCartByCustomer(user);
                model.addAttribute("shoppingCart", shoppingCart);
                model.addAttribute("listCartLineItem", cartService.getListCartItemByCart(shoppingCart));
                return "/cart/shopping-cart";
            } catch (ProductException | UserException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
                return "redirect:/main-page";
            }
        }
        @PostMapping("/cart/update-cart")
        public String updateCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                                              @RequestParam(value = "productId") Integer productId,
                                                              @RequestParam(value = "quantity") Integer quantity,
                                                              RedirectAttributes redirectAttributes ) {
            try {
                User customerUser = cartService.findUserById(customer.getUserId());
                if (productId == null || quantity == null || quantity <= 0) {
                    throw new IllegalArgumentException("Invalid productId or quantity.");
                }
                Product selectedProduct = cartService.getProductById(productId);
                cartService.addProductToCart(customerUser, selectedProduct, quantity);
                redirectAttributes.addFlashAttribute("message", "Thêm vào giỏ hàng thành công!");
                return "redirect:/cart?productId="+productId;
            } catch (ProductException | IllegalArgumentException | UserException ex) {
                redirectAttributes.addFlashAttribute("errMessage", ex.getMessage());
                return "redirect:/cart/productId="+productId;
            }
        }
        @GetMapping("/cart/clear")
        public String clearAllCardAndCartLineItem(@AuthenticationPrincipal ShopMeUserDetail customer,RedirectAttributes redirectAttributes) throws ProductException, UserException {
            User customerUser = this.cartService.findUserById(customer.getUserId());
            this.cartService.clearCard(customerUser);
            redirectAttributes.addFlashAttribute("message","Clear Cart Successfully");
            return "redirect:/cart/shopping-cart";

        }
    }
