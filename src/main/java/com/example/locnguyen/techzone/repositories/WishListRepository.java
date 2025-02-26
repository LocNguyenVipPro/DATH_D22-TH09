package com.example.locnguyen.techzone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.Product;
import com.example.locnguyen.techzone.models.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    public WishList findByProduct(Product product);

    public WishList findByUserIdAndProductId(Integer user_id, Integer product_id);

    public List<WishList> findWishListByUserId(int id);
}
