package com.example.dath.eshop.controllers;

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

import com.example.dath.eshop.exceptions.ProductException;
import com.example.dath.eshop.mappers.ProductRequestMapper;
import com.example.dath.eshop.models.Product;
import com.example.dath.eshop.requests.ProductRequest;
import com.example.dath.eshop.services.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String viewListProduct(
            @RequestParam(value = "search", required = false) String keyWord,
            @RequestParam(value = "isHide", required = false) Boolean isHide,
            Model model) {

        // Đảm bảo isHide có giá trị mặc định là true nếu không được truyền
        isHide = (isHide == null) ? true : isHide;

        // Set các thuộc tính động cho giao diện
        String buttonLabel = isHide ? "Show Deleted Product" : "Hide Deleted Product";
        String title = isHide ? "Product" : "Deleted Product";

        model.addAttribute("hideAndShowButton", buttonLabel);
        model.addAttribute("productTitle", title);
        model.addAttribute("pageTitle", "Product");
        model.addAttribute("isChoice", "Products");

        // Lấy danh sách sản phẩm theo các tham số
        model.addAttribute("listProduct", productService.findAll(null, keyWord, isHide));

        // Toggle trạng thái isHide
        model.addAttribute("isHide", !isHide);

        return "product/product";
    }

    @GetMapping("/products/add")
    public String viewAddProduct(Model model) {
        model.addAttribute("pageTitle", "Add Product");
        model.addAttribute("titleForm", "Add Product");
        model.addAttribute("ListProductCategory", productService.findAllCategory());
        model.addAttribute("productRequest", new ProductRequest());
        return "product/add-product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer deleteProductId, RedirectAttributes redirectAttributes) {
        return handleProductAction(deleteProductId, "delete", redirectAttributes);
    }

    @GetMapping("/products/restore/{id}")
    public String restoreProduct(@PathVariable("id") Integer restoreProductId, RedirectAttributes redirectAttributes) {
        return handleProductAction(restoreProductId, "restore", redirectAttributes);
    }

    private String handleProductAction(Integer productId, String action, RedirectAttributes redirectAttributes) {
        try {
            if ("delete".equals(action)) {
                productService.delete(productId);
                redirectAttributes.addFlashAttribute("Message", "Delete Successful Product With Id " + productId);
            } else if ("restore".equals(action)) {
                productService.restoreProduct(productId);
                redirectAttributes.addFlashAttribute("Message", "Restore Successful Product With Id " + productId);
            }
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/detail")
    public String productDetail(
            @RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product detailProduct = productService.findById(productId);
            model.addAttribute("pageTitle", "Product Detail | ID " + productId);
            model.addAttribute("productDetail", detailProduct);
            return "product/product-detail";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            model.addAttribute("pageTitle", "Edit Product | ID " + id);
            model.addAttribute("TitleForm", "Edit Product");
            model.addAttribute("ListProductCategory", productService.findAllCategory());
            model.addAttribute(
                    "productRequest", ProductRequestMapper.toProductRequest(productService.findById(id)));
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
            model.addAttribute("ListProductCategory", productService.findAllCategory());
            return "product/add-product-form";
        }

        try {
            Product productToSave = ProductRequestMapper.toProduct(productRequest);
            Integer idBeforeSaved = productToSave.getId();
            productService.saveProduct(productToSave, multipartFile);

            // Thông báo thành công
            String message = (idBeforeSaved != null) ? "Update Successful" : "Save Successful";
            redirectAttributes.addFlashAttribute("Message", message);
        } catch (IOException | ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/products";
    }
}
