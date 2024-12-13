package com.example.sm.minh.eshop.requests;

import com.example.sm.minh.eshop.models.ProductCategory;
import com.example.sm.minh.eshop.validators.annotations.SkuAndNameUnique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SkuAndNameUnique(idField = "id",skuField ="sku", nameField = "name")
public class ProductRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "Name Is Required")
    @Length(min = 2, message = "Username must be at least 6 characters")
    @Length(max = 30, message = "Username must not exceed 30 characters")
    private String name ;

    @NotNull
    @NotBlank(message = "SKU Is Required")
    @Length(min = 2, message = "SKU must be at least 2 characters")
    @Length(max = 15, message = "SKU must not exceed 15 characters")
    private String sku ;

    @NotNull
    @NotBlank(message = "Content Is Required")
    @Length(min = 2, message = "Content must be at least 2 characters")
    @Length(max = 150, message = "Content must not exceed 150 characters")
    private String content ;

    private String image;

    private float price;

    private float discountPrice;

    private float tax;
    private Boolean isActive;
    private Set<ProductCategory> ListProductCategories = new HashSet<>();

    public ProductRequest() {
    }
    public String loadImages()
    {
        System.out.println(this.id+this.image);
        if(this.image==null||this.image.isEmpty())
        {
            return "/images/products/img.png";
        }else
        {
            return "/images/products/"+this.id+"/"+this.image;
        }
    }
}
