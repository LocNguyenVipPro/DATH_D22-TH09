package com.example.sm.minh.eshop.controllers;

import com.example.sm.minh.eshop.models.UserProfile;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.exceptions.UserException;
import com.example.sm.minh.eshop.mappers.UserMapper;
import com.example.sm.minh.eshop.mappers.UserProfileMapper;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.UserService;
import com.example.sm.minh.eshop.requests.UserProfileRequest;
import com.example.sm.minh.eshop.requests.UserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/register")
    public String viewRegisterForm(Model model) {
        model.addAttribute("pageTitle", "Sign Up");
        model.addAttribute("titleForm", "Sign Up");
        model.addAttribute("isNewUser", true);
        model.addAttribute("userRequest", new UserRequest());
        return "user/register-form";
    }

    @GetMapping("/users/update_information")
    public String viewUpdateInformation(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User userLoggin = null;
        if (userDetails != null) {
            userLoggin = userService.findUserByUserName(userDetails.getUsername());
        }

        UserProfile userProfile = this.userService.getUserProfileByUsersId(userLoggin.getId());
        this.userService.setUpToUpdateForm(model,userProfile);
        return "user/update-information-user";
    }

    @PostMapping("/users/update_information/save")
    public String updateInformation(@AuthenticationPrincipal UserDetails userDetails,
                                    @Valid @ModelAttribute("userProfileRequest") UserProfileRequest userProfileRequest,
                                    BindingResult bindingResult,
                                    @RequestParam(value = "checkTick" ,required = false) String checked,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle", "Update User");
            model.addAttribute("titleForm", "Cập nhật thông tin cá nhân");
            if(checked.equals("false"))
            {
                model.addAttribute("isCheckGenderChoose", false);
            }
            return "user/update-information-user";
        }

        this.userService.updateProfile(UserProfileMapper.toUserProfile(userProfileRequest),userDetails);
        redirectAttributes.addFlashAttribute("Message","update Profile Successfully");
        return "redirect:/main-page";
    }


    @GetMapping("/users/edit")
    public String viewEditForm(@AuthenticationPrincipal ShopMeUserDetail userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findUserByUserName(userDetails.getUsername());
            UserRequest userRequest=UserMapper.toUserRequest(user);

            this.userService.prepareFormModel(model, "Edit User", false);
            model.addAttribute("pageTitle", "Edit User Id | "+userDetails.getUserId());
            model.addAttribute("titleForm", "Edit User");
            model.addAttribute("userRequest", userRequest);
            return "user/register-form";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/users/save")
    public String saveUser(@Valid  UserRequest createUserRequest,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) throws UserException {
        if (bindingResult.hasErrors()) {
            if (createUserRequest.getId() != null) {
                this.userService.prepareFormModel(model, "Edit User", false);
            } else {
                this.userService.prepareFormModel(model, "Sign Up", true);
            }
            return "user/register-form";
        }

        User userToSave=null;
        User users = UserMapper.toUser(createUserRequest);

        if (createUserRequest.getId() != null && createUserRequest.getId() != 0) {
            userToSave = this.userService.updateUser( users);
            redirectAttributes.addFlashAttribute("Message", "Update Information Successfully");
        } else {
            userToSave = this.userService.createNewUser(users);
            redirectAttributes.addFlashAttribute("Message", "Register Account Successfully");
        }

        this.userService.save(userToSave);
        return "redirect:/login-form";
    }
}