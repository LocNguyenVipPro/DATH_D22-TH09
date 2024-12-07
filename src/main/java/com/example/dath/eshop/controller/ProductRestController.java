package com.example.dath.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dath.eshop.dto.ProductDTO;
import com.example.dath.eshop.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/load-product")
    public List<ProductDTO> loadingProduct(
            @RequestParam(value = "rangePrice", required = false) String rangePrice,
            @RequestParam(value = "rangeSalePercent", required = false) String rangeSalePercent,
            @RequestParam(value = "categoryId", required = false) String categoryId) {

        // Xử lý các tham số đầu vào null hoặc trống
        rangePrice = sanitizeParam(rangePrice);
        rangeSalePercent = sanitizeParam(rangeSalePercent);
        categoryId = sanitizeParam(categoryId);

        // Lấy danh sách sản phẩm từ service
        List<ProductDTO> listProduct = productService.findByPrice(rangePrice, rangeSalePercent, categoryId);

        // Xử lý lại danh sách sản phẩm
        return listProduct.stream()
                .map(product -> {
                    if (product.getQuantityProduct() == null) {
                        product.setQuantityProduct(0L);  // Đảm bảo quantity không null
                    }
                    product.getProduct().setListProductCategories(null);  // Loại bỏ danh sách category
                    return product;
                })
                .collect(Collectors.toList());  // Trả về danh sách đã xử lý
    }

    // Phương thức giúp xử lý tham số null hoặc trống
    private String sanitizeParam(String param) {
        return (param == null || param.trim().isEmpty()) ? null : param;
    }
}
