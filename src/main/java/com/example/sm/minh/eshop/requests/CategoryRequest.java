package com.example.sm.minh.eshop.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.example.sm.minh.eshop.validators.annotations.SlugAndNameUnique;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SlugAndNameUnique(idField = "id", SlugField = "slug", nameField = "name")
public class CategoryRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "Tên là bắt buộc")
    @Length(min = 2, message = "Phải có ít nhất 2 kí tự")
    @Length(max = 100, message = "Không được vượt quá 100 kí tự")
    private String name;

    @NotNull
    @NotBlank(message = "Slug là bắt buộc")
    @Length(min = 2, message = "Slug phải có ít nhất 2 ký tự")
    @Length(max = 30, message = "Slug không được vượt quá 30 ký tự")
    private String slug;

    @NotNull
    @NotBlank(message = "Mô tả là bắt buộc")
    @Length(min = 6, message = "Mô tả phải có ít nhất 6 ký tự")
    @Length(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    private Boolean isActive;
    private String image;

    public String loadImages() {
        if (this.image == null || this.image.isEmpty()) {
            return "/images/products/img.png";
        } else {
            return "/images/categories/" + this.id + "/" + this.image;
        }
    }

    public CategoryRequest() {}
}
