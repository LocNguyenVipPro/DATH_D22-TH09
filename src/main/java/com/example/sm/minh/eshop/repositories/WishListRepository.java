package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {
    public WishList findByProduct(Product product);
    public WishList findByUserIdAndProductId(Integer user_id, Integer product_id);

    public List<WishList> findWishListByUserId(int id);
}
