package com.example.dath.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dath.eshop.exception.TokenException;
import com.example.dath.eshop.exception.UserException;
import com.example.dath.eshop.model.Token;
import com.example.dath.eshop.model.User;
import com.example.dath.eshop.service.AuthService;
import com.example.dath.eshop.service.TokenService;
import com.example.dath.eshop.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @GetMapping("/auth/forgot-password")
    public String viewForgotPassword(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "authenticated/forgot";
    }

    @PostMapping("/auth/send-email")
    public String sendEmail(Model model, @RequestParam("email") String email, RedirectAttributes redirectAttributes)
            throws UserException {
        try {
            this.authService.sendEmail(email);
            model.addAttribute("pageTitle", "Verification Code");
            model.addAttribute("email", email);
            return "authenticated/verification-code-form";
        } catch (UserException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Error sending verification email: " + ex.getMessage());
            return "redirect:/auth/forgot-password";
        }
    }

    @PostMapping("/auth/validator-token")
    public String checkValidToken(@RequestParam("token") String token, @RequestParam("email") String email, Model model)
            throws TokenException {
        try {
            User verifiedUser = this.userService.findUserByUserName(email);
            Token verificationToken = this.tokenService.getTokenByUser(verifiedUser);

            // Check token is valid
            if (!this.tokenService.isValidToken(verificationToken)
                    || !verificationToken.getToken().equals(token)
                    || !verificationToken.getUser().equals(verifiedUser)) {
                model.addAttribute("pageTitle", "Verification Code");
                model.addAttribute("email", email);
                model.addAttribute("errMessage", "Your verification code is invalid or has expired.");
                return "authenticated/verification-code-form";
            }

            model.addAttribute("pageTitle", "Change Password");
            model.addAttribute("email", email);
            model.addAttribute("token", token);
            return "authenticated/update-password";

        } catch (Exception ex) {
            model.addAttribute("pageTitle", "Verification Code");
            model.addAttribute("errMessage", "Error validating token: " + ex.getMessage());
            return "authenticated/verification-code-form";
        }
    }

    @PostMapping("/auth/save-update-password")
    public String updatePassword(
            @RequestParam("email") String email,
            @RequestParam("password") String newPassword,
            RedirectAttributes redirectAttributes) {
        try {
            User savedUser = this.userService.findUserByUserName(email);
            if (savedUser != null) {
                // Validate new password here if needed (e.g., check strength)
                savedUser.setPassword(passwordEncoder.encode(newPassword));
                this.tokenService.deleteToken(savedUser);
                this.userService.save(savedUser);
                redirectAttributes.addFlashAttribute("Message", "Password updated successfully!");
                return "redirect:/login-form";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
                return "redirect:/auth/forgot";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating password: " + e.getMessage());
            return "redirect:/auth/forgot";
        }
    }
}
