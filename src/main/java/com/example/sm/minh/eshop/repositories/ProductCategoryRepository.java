package com.example.sm.minh.eshop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sm.minh.eshop.models.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByName(String name);

    ProductCategory findBySlug(String slug);

    @Query("SELECT p from product_categories p where p.id = :id and p.isActive=:isHide")
    Optional<ProductCategory> findById(Integer id, Boolean isHide);

    @Query("SELECT DISTINCT pc FROM product_categories pc JOIN FETCH pc.listProducts p WHERE p.isActive = true")
    List<ProductCategory> findAllCategoriesWithProducts();

    @Query(
            "SELECT pc FROM product_categories pc WHERE concat(pc.name,pc.slug) LIKE %:searchValue% and pc.isActive=:isHide ")
    List<ProductCategory> findByNameContaining(String searchValue, Boolean isHide);

    @Query("SELECT pc FROM product_categories pc WHERE pc.isActive=:isHide ")
    List<ProductCategory> findAll(Boolean isHide);
}
