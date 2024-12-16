package com.example.sm.minh.eshop.controllers;

import java.io.IOException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.mappers.ProductRequestMapper;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.requests.ProductRequest;
import com.example.sm.minh.eshop.services.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productSerVice;

    @GetMapping("/products")
    public String viewListProduct(
            @RequestParam(value = "search", required = false) String keyWord,
            @RequestParam(value = "isHide", required = false) Boolean isHide,
            Model model) {

        if (isHide == null) {
            isHide = true;
        }

        if (isHide == true) {
            model.addAttribute("hideAndShowButton", "Show Deleted Product");
            model.addAttribute("productTitle", "Product");
        } else {
            model.addAttribute("hideAndShowButton", "Hide Deleted Product");
            model.addAttribute("productTitle", "Deleted Product");
        }

        model.addAttribute("pageTitle", "Product");
        model.addAttribute("isChoice", "Products");
        model.addAttribute("listProduct", productSerVice.findAll(null, keyWord, isHide));
        isHide = (isHide == false) ? true : false;

        model.addAttribute("isHide", isHide);

        return "product/product";
    }

    @GetMapping("/products/add")
    public String viewAddProduct(Model model) {
        model.addAttribute("pageTitle", "Add Product");
        model.addAttribute("titleForm", "Add Product");
        model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
        model.addAttribute("productRequest", new ProductRequest());
        return "product/add-product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer deleteProductId, RedirectAttributes redirectAttributes) {
        try {
            productSerVice.delete(deleteProductId);
            redirectAttributes.addFlashAttribute("Message", "Delete Successful Product With Id " + deleteProductId);
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/detail")
    public String productDetail(
            @RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product detailProduct = this.productSerVice.findById(productId);
            model.addAttribute("pageTitle", "Cart ID |" + productId);
            model.addAttribute("productDetail", detailProduct);
            return "product/product-detail";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/restore/{id}")
    public String restoreProduct(@PathVariable("id") Integer restoreProductId, RedirectAttributes redirectAttributes) {
        try {
            productSerVice.restoreProduct(restoreProductId);
            redirectAttributes.addFlashAttribute("Message", "Restore Successful Product With Id " + restoreProductId);
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            model.addAttribute("pageTitle", "Edit Product | ID " + id);
            model.addAttribute("TitleForm", "Edit Product");
            model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
            model.addAttribute(
                    "productRequest", ProductRequestMapper.toProductRequest(this.productSerVice.findById(id)));
            return "product/add-product-form";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }
    }

    @Transactional
    @PostMapping("/products/save")
    public String saveProduct(
            Model model,
            @Valid @ModelAttribute("productRequest") ProductRequest productRequest,
            BindingResult bindingResult,
            @RequestParam(value = "images", required = false) MultipartFile multipartFile,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Product");
            model.addAttribute("TitleForm", "Add Product");
            model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
            return "product/add-product-form";
        }
        try {
            Product ProductSaved = ProductRequestMapper.toProduct(productRequest);
            Integer idBeforeSaved = ProductSaved.getId();
            this.productSerVice.saveProduct(ProductSaved, multipartFile);
            if (idBeforeSaved != null) {
                redirectAttributes.addFlashAttribute("Message", "Update Successful");
            } else {
                redirectAttributes.addFlashAttribute("Message", "Save Successful");
            }
        } catch (IOException | ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/products";
    }
}
