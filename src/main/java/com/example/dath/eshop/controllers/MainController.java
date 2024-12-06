package com.example.dath.eshop.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dath.eshop.dto.ProductDTO;
import com.example.dath.eshop.exceptions.CategoryProductException;
import com.example.dath.eshop.models.ProductCategory;
import com.example.dath.eshop.services.ProductService;

@Controller
public class MainController {
    @Autowired
    private ProductService productSerVice;

    @GetMapping("/main-page")
    public String MainFile(
            @RequestParam(value = "category", required = false) Integer categoryId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "isChoiceCategory", required = false) String isChoiceCategory,
            Model model) {

        try {
            // Lấy danh sách danh mục sản phẩm
            List<ProductCategory> listCategory = productSerVice.findAllCategoryContainProduct();
            model.addAttribute("listCategory", listCategory);

            // Lấy danh sách sản phẩm đã được sắp xếp theo số lượng bán
            List<ProductDTO> orderedProducts = productSerVice.productOrderMost();

            // Lấy danh sách sản phẩm đã lọc theo categoryId và search
            List<ProductDTO> filteredProducts = productSerVice.findAll(categoryId, search, true);

            // Tính toán phần trăm giảm giá và số lượng sản phẩm
            Map<Integer, Integer> salePercentMap = filteredProducts.stream()
                    .collect(Collectors.toMap(
                            productDTO -> productDTO.getProduct().getId(),
                            productDTO -> productSerVice.calculateRoundedPercent(productDTO)
                    ));

            Map<Integer, String> saleItemMap = filteredProducts.stream()
                    .collect(Collectors.toMap(
                            productDTO -> productDTO.getProduct().getId(),
                            productDTO -> productSerVice.formatQuantity(productDTO.getQuantityProduct())
                    ));

            // Xử lý categoryId nếu có
            if (categoryId != null) {
                ProductCategory productCategory = productSerVice.getCategoryById(categoryId);
                if (productCategory != null) {
                    model.addAttribute("selectCategory", productCategory);
                } else {
                    model.addAttribute("errorMessage", "Category not found.");
                }
            }

            // Thiết lập các thuộc tính cho trang
            model.addAttribute("search", search);
            model.addAttribute("pageTitle", "SDN Shop");
            model.addAttribute("category", categoryId);
            model.addAttribute("listProduct", filteredProducts);
            model.addAttribute("salePercent", salePercentMap);
            model.addAttribute("saleItemMap", saleItemMap);
            model.addAttribute("isChoice", "Shop");
            model.addAttribute("isChoiceCategory", isChoiceCategory);
            model.addAttribute("productsOrderedMost", orderedProducts);

        } catch (CategoryProductException ex) {
            // Xử lý ngoại lệ nếu có lỗi khi tìm danh mục sản phẩm
            model.addAttribute("errorMessage", "Error occurred while fetching category products: " + ex.getMessage());
        } catch (Exception ex) {
            // Xử lý các lỗi khác (nếu có)
            model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        }

        return "main-page";
    }

    @GetMapping("/login-form")
    public String LoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        // Xử lý lỗi đăng nhập nếu có
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        model.addAttribute("pageTitle", "Login");
        return "login-form";
    }
}
