package com.example.dath.eshop.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, unique = true)
    private String name;

    @Column(name = "sku", length = 15, unique = true)
    private String sku;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "price")
    private float price;

    @Column(name = "discount_price")
    private float discountPrice;

    @Column(name = "tax")
    private float tax;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "products_categories",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Set<ProductCategory> listProductCategories = new HashSet<>();

    @Column(name = "stock_quantity") // Thêm trường stockQuantity
    private int stockQuantity; // Định nghĩa số lượng tồn kho

    public Product() {}

    public Product(
            String name, String sku, String content, float price, float discountPrice, float tax, int stockQuantity) {
        this.name = name;
        this.sku = sku;
        this.content = content;
        this.price = price;
        this.discountPrice = discountPrice;
        this.tax = tax;
        this.stockQuantity = stockQuantity; // Khởi tạo số lượng tồn kho
        this.createdAt = new Date();
    }

    // Các phương thức hỗ trợ
    public void addProductCate(ProductCategory productCategory) {
        this.listProductCategories.add(productCategory);
    }

    public String loadImages() {
        if (this.image == null || this.image.isEmpty()) {
            return "/images/products/img.png";
        } else {
            return "/images/products/" + this.id + "/" + this.image;
        }
    }

    // Getter và Setter cho stockQuantity
    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Product{" + "id="
                + id + ", name='"
                + name + '\'' + ", sku='"
                + sku + '\'' + ", content='"
                + content + '\'' + ", image='"
                + image + '\'' + ", price="
                + price + ", discountPrice="
                + discountPrice + ", tax="
                + tax + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + ", deletedAt="
                + deletedAt + ", isActive="
                + isActive + ", stockQuantity="
                + stockQuantity + // Hiển thị stockQuantity
                '}';
    }
}
