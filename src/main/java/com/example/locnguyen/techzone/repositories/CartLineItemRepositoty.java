package com.example.locnguyen.techzone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Cart;
import com.example.locnguyen.techzone.models.CartLineItem;
import com.example.locnguyen.techzone.models.Product;

@Repository
public interface CartLineItemRepositoty extends JpaRepository<CartLineItem, Integer> {
    public CartLineItem findByCartIdAndProductId(Cart cartId, Product productId);

    @Query("SELECT c FROM CartLineItem c WHERE c.productId = :productId AND c.productId.isActive = true")
    public List<CartLineItem> findByProductId(Product productId);

    @Query("SELECT c FROM CartLineItem c WHERE c.cartId = :cartId AND c.productId.isActive = true")
    public List<CartLineItem> findByCartId(Cart cartId);
}
