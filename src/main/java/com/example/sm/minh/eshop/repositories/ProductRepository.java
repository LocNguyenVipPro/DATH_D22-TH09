package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("select p from Product p where p.id =:id and p.isActive=true")
    public Optional<Product>findById(Integer id);

    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc = :category")
    public List<Product> getProductsRelated(ProductCategory category);

    @Query("select p from Product  p where p.isActive=false and p.id=:id")
    public Optional<Product>findProductByIsActiveIsFalse(Integer id);

    public Product findProductByName(String name);
    public Product findProductBySku(String sku);



    @Query("SELECT p, SUM(oli.quantity) AS totalOrdered " +
            "FROM Product p " +
            "left JOIN p.ListProductCategories pc " +
            "left JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE pc.id = :categoryId AND p.isActive = :isHide " +
            "GROUP BY p.id, p ")
    List<Object[]> findAllByCategoryId(@Param("categoryId") Integer categoryId, @Param("isHide") Boolean isHide);

    @Query("SELECT p" +
            " FROM Product p " +
            " left JOIN p.ListProductCategories pc " +
            " left JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            " WHERE pc.id = :categoryId AND p.isActive = :isHide ")
    List<Product> findAll(@Param("categoryId") Integer categoryId, @Param("isHide") Boolean isHide);



    @Query("SELECT p, COALESCE(sum (oli.quantity),0) as totalOrdered " +
            "FROM Product p " +
            "LEFT JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE CONCAT(p.name, p.content) LIKE %:keyword% AND p.isActive = :isHide " +
            "GROUP BY p")
    List<Object[]> findAll(@Param("keyword") String keyword, @Param("isHide") Boolean isHide);



    @Query("SELECT p, COALESCE(SUM(oli.quantity), 0) AS totalOrdered    " +
            "FROM Product p " +
            "left JOIN p.ListProductCategories pc " +
            "left JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE (:categoryId IS NULL OR pc.id = :categoryId) " +
            "  AND CONCAT(p.name, p.sku, pc.name) LIKE %:keyword% " +
            "  AND p.isActive = :isHide " +
            "GROUP BY p.id, p")
    List<Object[]> findAll(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword, @Param("isHide") Boolean isHide);


    @Query("SELECT p, COALESCE(SUM(oli.quantity), 0) AS total_sold " +
            "FROM Product p " +
            "LEFT JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE p.isActive = :isHide " +
            "GROUP BY p")
    List<Object[]> findAllProductsAndTotalSold(@Param("isHide") Boolean isHide);
    @Query("SELECT p, COALESCE(SUM(oli.quantity), 0) AS total_sold " +
            "FROM Product p " +
            "LEFT JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE (EXISTS (SELECT 1 FROM p.ListProductCategories pc WHERE pc.id = :categoryId) OR :categoryId IS NULL) " +
            "AND p.isActive = true " +
            "AND ((:minPrice IS NULL OR :maxPrice IS NULL) OR (p.discountPrice >= :minPrice AND p.discountPrice <= :maxPrice)) " +
            "AND ((:minRangePercent IS NULL OR :maxRangePercent IS NULL) OR ((p.price - p.discountPrice) / p.price * 100 >= :minRangePercent AND (p.price - p.discountPrice) / p.price * 100 <= :maxRangePercent)) " +
            "GROUP BY p.id")

    List<Object[]> findByPriceSalePercentAndCategory(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minRangePercent") Integer minRangePercent,
            @Param("maxRangePercent") Integer maxRangePercent,
            @Param("categoryId") Integer categoryId);



}