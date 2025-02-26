package com.example.locnguyen.techzone.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.locnguyen.techzone.dto.ProductDTO;
import com.example.locnguyen.techzone.exceptions.CategoryProductException;
import com.example.locnguyen.techzone.models.ProductCategory;
import com.example.locnguyen.techzone.services.ProductService;

@Controller
public class MainController {
    @Autowired
    private ProductService productSerVice;

    @GetMapping("/main-page")
    public String MainFile(
            @RequestParam(value = "category", required = false) Integer categoryId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "isChoiceCategory", required = false) String isChoiceCategory,
            Model model)
            throws CategoryProductException {
        List<ProductCategory> listCategory = productSerVice.findAllCategoryContainProduct();
        model.addAttribute("listCategory", listCategory);
        ArrayList<ProductDTO> productDTOS = null;

        try {
            if (categoryId != null) {
                ProductCategory productCategory = this.productSerVice.getCategoryById(categoryId);
                model.addAttribute("selectCategory", productCategory);
            }

            if (categoryId == null && search == null) {
                productDTOS = productSerVice.productOrderMost();
            }

            List<ProductDTO> listProduct = productSerVice.findAll(categoryId, search, true);
            Map<Integer, Integer> salePercentMap = new HashMap<>();
            Map<Integer, String> saleItemMap = new HashMap<>();

            for (ProductDTO productDTO : listProduct) {
                int percent = this.productSerVice.calculateRoundedPercent(productDTO);
                if (percent > 0) salePercentMap.put(productDTO.getProduct().getId(), percent);
                saleItemMap.put(
                        productDTO.getProduct().getId(),
                        this.productSerVice.formatQuantity(productDTO.getQuantityProduct()));
            }

            model.addAttribute("search", search);
            model.addAttribute("pageTitle", "TechZone");
            model.addAttribute("category", categoryId);
            model.addAttribute("listProduct", listProduct);
            model.addAttribute("salePercent", salePercentMap);
            model.addAttribute("saleItemMap", saleItemMap);
            model.addAttribute("isChoice", "Shop");
            model.addAttribute("isChoiceCategory", isChoiceCategory);
            model.addAttribute("productsOrderedMost", productDTOS);
        } catch (CategoryProductException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "main-page";
    }

    @GetMapping("/login-form")
    public String LoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("errorMessage", "UserName or password is incorrect");
        }

        model.addAttribute("pageTitle", "Login");
        return "login-form";
    }

    @GetMapping("/access-denied")
    public String AccessDenied() {
        return "access-denied";
    }
}
