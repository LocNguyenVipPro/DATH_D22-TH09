package com.example.dath.eshop.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dath.eshop.exception.CartLineItemException;
import com.example.dath.eshop.exception.ProductException;
import com.example.dath.eshop.exception.UserException;
import com.example.dath.eshop.model.*;
import com.example.dath.eshop.repository.CartLineItemRepositoty;
import com.example.dath.eshop.repository.CartRepository;
import com.example.dath.eshop.repository.ProductRepository;
import com.example.dath.eshop.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private UserRepository userRepository;

    // Get product by ID with exception handling
    public Product getProductById(Integer id) throws ProductException {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductException("Cannot find product with ID: " + id));
    }

    // Find user by ID with exception handling
    public User findUserById(Integer id) throws UserException {
        return userRepository.findById(id).orElseThrow(() -> new UserException("Cannot find user with ID: " + id));
    }

    // Get all cart items by cart
    public List<CartLineItem> getListCartItemByCart(Cart cart) {
        return cartLineItemRepositoty.findByCartId(cart);
    }

    // Get cart by customer (user)
    public Cart getCartByCustomer(User customer) throws ProductException {
        return cartRepository
                .findByUserId(customer.getId())
                .orElseThrow(() -> new ProductException("Cannot find cart for user with ID: " + customer.getId()));
    }

    // Add product to cart, check stock quantity first
    public void addProductToCart(User customer, Product selectedProduct, Integer quantity) {
        try {
            if (selectedProduct == null || selectedProduct.getStockQuantity() <= 0) {
                throw new ProductException("Product is out of stock");
            }

            Cart cart = getOrCreateCart(customer);
            updateCartInfo(cart, selectedProduct, quantity);
        } catch (ProductException e) {
            // Xử lý lỗi, ví dụ: log lỗi, thông báo cho người dùng, v.v.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Get or create cart for a customer
    private Cart getOrCreateCart(User customer) {
        Cart cart = cartRepository.findByUserId(customer.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(customer);
            return cartRepository.save(newCart);
        });
        cart.setUpdatedAt(new Date()); // Manually setting updatedAt if necessary
        return cart;
    }

    // Update cart info when adding a product
    private void updateCartInfo(Cart cart, Product selectedProduct, Integer quantity) {
        CartLineItem cartLineItem = getOrCreateCartLineItem(cart, selectedProduct);
        updateCartAndCartItem(cart, cartLineItem, selectedProduct, quantity);
    }

    // Get or create cart line item
    private CartLineItem getOrCreateCartLineItem(Cart cart, Product selectedProduct) {
        CartLineItem cartLineItem = cartLineItemRepositoty.findByCartIdAndProductId(cart, selectedProduct);
        if (cartLineItem == null) {
            cartLineItem = new CartLineItem();
            cartLineItem.setCreatedAt(new Date());
        } else {
            cartLineItem.setUpdatedAt(new Date());
        }
        return cartLineItem;
    }

    // Update cart and cart line item
    private void updateCartAndCartItem(Cart cart, CartLineItem cartLineItem, Product buyProduct, Integer quantity) {
        // Calculate tax amount and price before tax
        Float taxAmount = calculateTaxAmount(buyProduct, quantity);
        Float priceBeforeTax = buyProduct.getDiscountPrice() * quantity;

        // Update cart totals
        cart.setCountItem(cart.getCountItem() + quantity);
        cart.setTaxAmount(cart.getTaxAmount() + taxAmount);
        cart.setTotalAmount(cart.getTotalAmount() + priceBeforeTax + taxAmount);

        // Update cart line item totals
        cartLineItem.setQuantity(cartLineItem.getQuantity() + quantity);
        cartLineItem.setSubTotalAmount(cartLineItem.getSubTotalAmount() + priceBeforeTax);
        cartLineItem.setTaxTotalAmount(cartLineItem.getTaxTotalAmount() + taxAmount);
        cartLineItem.setTotalAmount(cartLineItem.getSubTotalAmount() + cartLineItem.getTaxTotalAmount());
        cartLineItem.setProductId(buyProduct);
        cartLineItem.setCartId(cart);

        // Save updated cart line item and cart
        cartLineItemRepositoty.save(cartLineItem);
        cartRepository.save(cart);
    }

    // Calculate tax amount
    private Float calculateTaxAmount(Product selectedProduct, Integer quantity) {
        return (selectedProduct.getDiscountPrice() * quantity * selectedProduct.getTax()) / 100;
    }

    // Delete cart line item
    public void deleteCartLineItem(Integer cartLineItemId, User customer) throws CartLineItemException {
        CartLineItem cartLineItem = cartLineItemRepositoty
                .findById(cartLineItemId)
                .orElseThrow(() -> new CartLineItemException("Cannot find Cart Line Item with ID: " + cartLineItemId));

        // Unlink cart line item from cart
        cartLineItem.setCartId(null);
        cartLineItemRepositoty.save(cartLineItem);

        // Update cart totals
        Cart cart = cartRepository
                .findByUserId(customer.getId())
                .orElseThrow(() -> new CartLineItemException("Cart not found for user"));
        cart.setCountItem(cart.getCountItem() - cartLineItem.getQuantity());
        cart.setTaxAmount(cart.getTaxAmount() - cartLineItem.getTaxTotalAmount());
        cart.setTotalAmount(cart.getTotalAmount() - cartLineItem.getTotalAmount());

        // Mark cart as deleted if no items left
        if (cartLineItemRepositoty.findByCartId(cart).isEmpty()) {
            cart.setDeletedAt(new Date());
            cart.setIsActive(false); // Mark cart as inactive
        }

        cartRepository.save(cart);
        cartLineItemRepositoty.delete(cartLineItem);
    }

    // Clear cart for a user
    public void clearCard(User customer) throws ProductException {
        Cart cart = cartRepository
                .findByUserId(customer.getId())
                .orElseThrow(() -> new ProductException("Cart not found for user"));

        // Remove all line items
        List<CartLineItem> cartLineItems = cartLineItemRepositoty.findByCartId(cart);
        for (CartLineItem lineItem : cartLineItems) {
            lineItem.setCartId(null);
        }
        cartLineItemRepositoty.deleteAll(cartLineItems);

        // Mark the cart as deleted
        cart.setDeletedAt(new Date());
        cart.setIsActive(false); // Mark cart as inactive
        cartRepository.save(cart);
    }

    // Get related products for a given cart product
    public Set<Product> getRelatedProduct(Product cartProduct) {
        Set<ProductCategory> productCategories = cartProduct.getListProductCategories();
        Set<Product> relatedProducts =
                new HashSet<>(productRepository.getProductsRelated((ProductCategory) productCategories));
        relatedProducts.remove(cartProduct);
        return relatedProducts;
    }
}
