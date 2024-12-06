package com.example.dath.eshop.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dath.eshop.exceptions.UserException;
import com.example.dath.eshop.mappers.UserMapper;
import com.example.dath.eshop.mappers.UserProfileMapper;
import com.example.dath.eshop.models.User;
import com.example.dath.eshop.models.UserProfile;
import com.example.dath.eshop.requests.UserProfileRequest;
import com.example.dath.eshop.requests.UserRequest;
import com.example.dath.eshop.securities.ShopMeUserDetail;
import com.example.dath.eshop.services.UserService;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;

    private void prepareModelForUserForm(Model model, String pageTitle, String titleForm, boolean isNewUser) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("titleForm", titleForm);
        model.addAttribute("isNewUser", isNewUser);
    }

    @GetMapping("/users/register")
    public String viewRegisterForm(Model model) {
        prepareModelForUserForm(model, "Sign Up", "Sign Up", true);
        model.addAttribute("userRequest", new UserRequest());
        return "user/register-form";
    }

    @GetMapping("/users/update_information")
    public String viewUpdateInformation(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User userLoggin = userService.findUserByUserName(userDetails.getUsername());
        if (userLoggin == null) {
            return "redirect:/login";
        }

        UserProfile userProfile = userService.getUserProfileByUsersId(userLoggin.getId());
        userService.setUpToUpdateForm(model, userProfile);
        return "user/update-information-user";
    }

    @PostMapping("/users/update_information/save")
    public String updateInformation(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("userProfileRequest") UserProfileRequest userProfileRequest,
            BindingResult bindingResult,
            @RequestParam(value = "checkTick", required = false) String checked,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Update User");
            model.addAttribute("titleForm", "Update User Profile");
            if ("false".equals(checked)) {
                model.addAttribute("isCheckGenderChoose", false);
            }
            return "user/update-information-user";
        }

        userService.updateProfile(UserProfileMapper.toUserProfile(userProfileRequest), userDetails);
        redirectAttributes.addFlashAttribute("Message", "Update Profile Successfully");
        return "redirect:/main-page";
    }

    @GetMapping("/users/edit")
    public String viewEditForm(@AuthenticationPrincipal ShopMeUserDetail userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findUserByUserName(userDetails.getUsername());
        UserRequest userRequest = UserMapper.toUserRequest(user);
        prepareModelForUserForm(model, "Edit User Id | " + userDetails.getUserId(), "Edit User", false);
        model.addAttribute("userRequest", userRequest);
        return "user/register-form";
    }

    @PostMapping("/users/save")
    public String saveUser(
            @Valid UserRequest createUserRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes)
            throws UserException {
        if (bindingResult.hasErrors()) {
            if (createUserRequest.getId() != null) {
                prepareModelForUserForm(model, "Edit User", "Edit User", false);
            } else {
                prepareModelForUserForm(model, "Sign Up", "Sign Up", true);
            }
            return "user/register-form";
        }

        User userToSave = UserMapper.toUser(createUserRequest);
        if (createUserRequest.getId() != null && createUserRequest.getId() != 0) {
            userToSave = userService.updateUser(userToSave);
            redirectAttributes.addFlashAttribute("Message", "Update Information Successfully");
        } else {
            userToSave = userService.createNewUser(userToSave);
            redirectAttributes.addFlashAttribute("Message", "Register Account Successfully");
        }

        userService.save(userToSave);
        return "redirect:/login-form";
    }
}

