package com.example.dath.eshop.dto;

import com.example.dath.eshop.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Product product;
    private Long quantityProduct;
}
