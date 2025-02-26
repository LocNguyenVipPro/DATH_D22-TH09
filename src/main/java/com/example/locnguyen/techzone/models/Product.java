package com.example.locnguyen.techzone.models;

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

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "products_categories",
            joinColumns = {@JoinColumn(name = "product_categories_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private Set<ProductCategory> ListProductCategories = new HashSet<>();

    public Set<ProductCategory> getListProductCategories() {
        return ListProductCategories;
    }

    public void setListProductCategories(Set<ProductCategory> listProductCategories) {
        ListProductCategories = listProductCategories;
    }

    public String loadImages() {
        if (this.image == null || this.image.isEmpty()) {
            return "/images/products/img.png";
        } else {
            return "/images/products/" + this.id + "/" + this.image;
        }
    }

    @Override
    public String toString() {
        return "Product{" + "id="
                + id + ", name='"
                + name + '\'' + ", Sku='"
                + sku + '\'' + ", Content='"
                + content + '\'' + ", image='"
                + image + '\'' + ", price="
                + price + ", discount_price="
                + discountPrice + ", tax="
                + tax + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + ", deletedAt="
                + deletedAt + ", isActive="
                + isActive + '}';
    }

    public Product(String name, String sku, String content, float price, float discount_price, float tax) {
        this.name = name;
        sku = sku;
        content = content;
        this.price = price;
        this.discountPrice = discount_price;
        this.tax = tax;
        this.createdAt = new Date();
    }

    public void addProductCate(ProductCategory productCategories) {
        this.ListProductCategories.add(productCategories);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Product() {}
}
