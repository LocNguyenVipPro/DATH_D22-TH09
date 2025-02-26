package com.example.locnguyen.techzone.controllers;

import java.io.IOException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.locnguyen.techzone.exceptions.ProductCategoryException;
import com.example.locnguyen.techzone.exceptions.ProductException;
import com.example.locnguyen.techzone.mappers.CategoryRequestMapper;
import com.example.locnguyen.techzone.models.ProductCategory;
import com.example.locnguyen.techzone.requests.CategoryRequest;
import com.example.locnguyen.techzone.services.ProductCategoryService;

@Controller
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoriesSerVice;

    @GetMapping("/category")
    public String viewListProduct(
            @RequestParam(value = "search", required = false) String serachValue,
            @RequestParam(value = "isHide", required = false) Boolean isHide,
            Model model) {

        if (isHide == null) {
            isHide = true;
        }

        if (isHide == true) {
            model.addAttribute("hideAndShowButton", "Show Deleted Category");
            model.addAttribute("productTitle", "Product Category");
        } else {
            model.addAttribute("hideAndShowButton", "Hide Deleted Category");
            model.addAttribute("productTitle", "Deleted Product Category");
        }

        model.addAttribute("pageTitle", "Category");
        model.addAttribute("isChoice", "Category");
        model.addAttribute("listCate", this.productCategoriesSerVice.findAll(serachValue, isHide));
        isHide = (isHide == false) ? true : false;
        model.addAttribute("isHide", isHide);

        return "category/category";
    }

    @GetMapping("/category/add")
    public String viewAddCategory(Model model) {

        model.addAttribute("categoryRequest", new CategoryRequest());
        model.addAttribute("pageTitle", "Category");
        model.addAttribute("titleForm", "Add Category");
        model.addAttribute("isNewUser", true);
        return "category/add-category-form";
    }

    @PostMapping("/category/save")
    public String saveCategory(
            @Valid @ModelAttribute("categoryRequest") CategoryRequest categoryRequest,
            BindingResult bindingResult,
            @RequestParam("images") MultipartFile multipartFile,
            Model model,
            RedirectAttributes redirectAttributes)
            throws ProductCategoryException, IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Category");
            model.addAttribute("TitleForm", "Add Category");
            model.addAttribute("isNewUser", true);
            return "category/add-category-form";
        }
        ProductCategory savedCategory = CategoryRequestMapper.toCategory(categoryRequest);
        if (savedCategory.getId() == null || savedCategory.getId() == 0) {
            redirectAttributes.addFlashAttribute("Message", "Save Successfully Category " + savedCategory.getName());
        } else {
            redirectAttributes.addFlashAttribute("Message", "Updated Successfully Category " + savedCategory.getName());
        }
        this.productCategoriesSerVice.saveCategory(savedCategory, multipartFile);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.deleteCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successfully Category With Id " + id);
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ProductException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/category";
    }

    @GetMapping("/category/restore/{id}")
    public String restoreCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.restoreCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Restore Successful Category With Id " + id);
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/category";
    }

    @GetMapping("/category/edit/{id}")
    public String update(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductCategory editCategory = this.productCategoriesSerVice.findById(id, true);

            model.addAttribute("pageTitle", "Edit Category ID | " + id);
            model.addAttribute("titleForm", "Edit Category");
            model.addAttribute("categoryRequest", CategoryRequestMapper.toCategoryRequest(editCategory));
            return "category/add-category-form";
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}
