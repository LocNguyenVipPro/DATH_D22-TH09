package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.exceptions.UserException;
import com.example.sm.minh.eshop.models.*;
import com.example.sm.minh.eshop.exceptions.CartLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.CartLineItemRepositoty;
import com.example.sm.minh.eshop.repositories.CartReposttory;
import com.example.sm.minh.eshop.repositories.ProductRepository;
import com.example.sm.minh.eshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartReposttory cartReposttory;

    @Autowired
    private ProductRepository productsRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private UserRepository userRepository;

    public Product getProductById(Integer id) throws ProductException {
        Optional<Product> productOptional = productsRepository.findById(id);
        return productOptional.orElseThrow(() -> new ProductException("Cannot find product with ID: " + id));
    }

    public User findUserById(Integer id) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new UserException("Cannot find user with ID: " + id));
    }

    public List<CartLineItem> getListCartItemByCart(Cart cart) throws ProductException {
        return this.cartLineItemRepositoty.findByCartId(cart);
    }

    public Cart getCartByCustomer(User customer) throws ProductException {
        return this.cartReposttory.findByUserId(customer.getId());
    }

    public void addProductToCart(User customer, Product selectProduct, Integer quantity) throws ProductException {
        Cart cart = getOrCreateCart(customer);
        updateCartInfo(cart, selectProduct, quantity);
    }

    private Cart getOrCreateCart(User customer) {
        Cart cart = this.cartReposttory.findByUserId(customer.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(customer);
            cart = this.cartReposttory.save(cart);
        } else {
            cart.setUpdatedAt(new Date());
        }

        return cart;
    }

    private void updateCartInfo(Cart cart, Product selectProduct, Integer quantity) {
        CartLineItem cartLineItem = getOrCreateCartLineItem(cart, selectProduct);
        updateCartAndCartItem(cart, cartLineItem, selectProduct, quantity);
    }

    private CartLineItem getOrCreateCartLineItem(Cart cart, Product selectProduct) {
        CartLineItem cartLineItem = this.cartLineItemRepositoty.findByCartIdAndProductId(cart, selectProduct);

        if (cartLineItem == null) {
            cartLineItem = new CartLineItem();
            cartLineItem.setCreatedAt(new Date());
        } else {
            cartLineItem.setUpdatedAt(new Date());
        }

        return cartLineItem;
    }

    private void updateCartAndCartItem(Cart cart, CartLineItem cartLineItem, Product buyProduct, Integer quantity) {
        //calculator data
        Float taxAmount = calculateTaxAmount(buyProduct, quantity);
        Float priceBeforeTax = buyProduct.getDiscountPrice() * quantity;

        //update data for cart
        cart.setCountItem(cart.getCountItem() + quantity);
        cart.setTaxAmount(cart.getTaxAmount() + taxAmount);
        cart.setTotalAmount(cart.getTotalAmount() + taxAmount + priceBeforeTax);

        //update data for cartLineItem
        cartLineItem.setQuantity(cartLineItem.getQuantity() + quantity);
        cartLineItem.setSubTotalAmount(cartLineItem.getSubTotalAmount() + priceBeforeTax);
        cartLineItem.setTaxTotalAmount(cartLineItem.getTaxTotalAmount() + taxAmount);
        cartLineItem.setTotalAmount(cartLineItem.getSubTotalAmount() + cartLineItem.getTaxTotalAmount());
        cartLineItem.setProductId(buyProduct);
        cartLineItem.setCartId(cart);

        this.cartLineItemRepositoty.save(cartLineItem);
        this.cartReposttory.save(cart);
    }

    private Float calculateTaxAmount(Product selectProduct, Integer quantity) {
        return ((selectProduct.getDiscountPrice() * quantity) / 100) * selectProduct.getTax();
    }

    public void deleteCartLineItem(Integer cardLineItemId, User customer) throws CartLineItemException {
        Optional<CartLineItem> cartLineItemsOptional = this.cartLineItemRepositoty.findById(cardLineItemId);
        CartLineItem cartLineItems = cartLineItemsOptional.orElseThrow(() -> new CartLineItemException("Cannot find Cart Line Item with ID: " + cardLineItemId));

        // Unlink Cart from CartLineItem
        cartLineItems.setCartId(null);
        this.cartLineItemRepositoty.save(cartLineItems);

        // Update Cart
        Cart cart=this.cartReposttory.findByUserId(customer.getId());
        cart.setCountItem(cart.getCountItem()-cartLineItems.getQuantity());
        cart.setTaxAmount(cart.getTaxAmount()-cartLineItems.getTaxTotalAmount());
        cart.setTotalAmount(cart.getTotalAmount()-cartLineItems.getTotalAmount());

        // Check if the cart is empty after removing the cart line item
        List<CartLineItem>cartLineItemContailCartId=this.cartLineItemRepositoty.findByCartId(cart);
        if (cartLineItemContailCartId.isEmpty())
        {
            cart.setDeletedAt(new Date());
        }

        this.cartReposttory.save(cart);
        this.cartLineItemRepositoty.delete(cartLineItems);
    }

    public void clearCard(User Customer)
    {
        Cart cartClear=this.cartReposttory.findByUserId(Customer.getId());

        if(cartClear!=null)
        {
            List<CartLineItem>cartLineItemList=this.cartLineItemRepositoty.findByCartId(cartClear);
            List<CartLineItem>deleteCartLineItem=new ArrayList<>();
            for(CartLineItem cartLineItem : cartLineItemList)
            {
                cartLineItem.setCartId(null);
                deleteCartLineItem.add(cartLineItem);
            }
            this.cartLineItemRepositoty.saveAll(deleteCartLineItem);
            this.cartLineItemRepositoty.deleteAll(deleteCartLineItem);
            cartClear.setUser(null);
            this.cartReposttory.save(cartClear);
            this.cartReposttory.delete(cartClear);
        }

    }


    public Set<Product> getRelatedProduct(Product cartProduct) {
        Set<Product> relatedProduct=new HashSet<>();
        for (ProductCategory productCategory : cartProduct.getListProductCategories())
        {
            relatedProduct.addAll( this.productsRepository.getProductsRelated(productCategory));
        }
        relatedProduct.remove(cartProduct);
        return relatedProduct;
    }
}