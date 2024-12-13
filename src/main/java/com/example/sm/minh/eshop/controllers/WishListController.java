package com.example.sm.minh.eshop.controllers;

import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.WishList;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.ProductService;
import com.example.sm.minh.eshop.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductService productService;

    @GetMapping("/wishlist")
    public String getAllWishList(@AuthenticationPrincipal ShopMeUserDetail customer,Model model) throws ProductException {
        List<WishList> wishList = wishListService.findAll(customer.getUserId());
        List<Product> productList = new ArrayList<>();
        for(int i=0; i<wishList.size(); i++) {
            productList.add(wishList.get(i).getProduct());
        }
        model.addAttribute("listProduct", productList);
        return "wishList/wish-list";
    }

    @PostMapping("/wishlist")
    public String toggleWishlist(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttributes
    ) throws ProductException {
        Product product = this.productService.findById(productId);
        WishList existingWishlist = wishListService.wishListRepository.findByUserIdAndProductId(customer.getUserId(), productId);
        if (existingWishlist == null) {
            WishList wishListCreate = new WishList();
            wishListCreate.setUser(customer.getUsers());
            wishListCreate.setProduct(product);
            this.wishListService.saveWishList(wishListCreate);
        }
        redirectAttributes.addFlashAttribute("message", "Thêm Vào mục yêu thích thành công");
        return "redirect:/cart?productId=" + productId;
    }
    @GetMapping("/wishlist/delete/{id}")
    public String deleteCategory(@AuthenticationPrincipal ShopMeUserDetail customer,@PathVariable int id, RedirectAttributes redirectAttributes) {
        this.wishListService.remove(customer,id);
        redirectAttributes.addFlashAttribute("Message", "Xoá ra mục yêu thích thành công");
        return "redirect:/wishlist";
    }}
