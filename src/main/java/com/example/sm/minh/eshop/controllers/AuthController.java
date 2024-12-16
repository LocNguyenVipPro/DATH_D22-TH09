package com.example.sm.minh.eshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.sm.minh.eshop.exceptions.TokenException;
import com.example.sm.minh.eshop.exceptions.UserException;
import com.example.sm.minh.eshop.models.Token;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.services.AuthService;
import com.example.sm.minh.eshop.services.TokenService;
import com.example.sm.minh.eshop.services.UserService;

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
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/auth/forgot-password";
        }
    }

    @PostMapping("/auth/validator-token")
    public String checkValidToken(@RequestParam("token") String token, @RequestParam("email") String email, Model model)
            throws TokenException {
        User verifiedUser = this.userService.findUserByUserName(email);
        Token verificationToken = this.tokenService.getTokenByUser(verifiedUser);

        // Check token is valid
        if (!(this.tokenService.isValidToken(verificationToken)
                && verificationToken.getToken().equals(token)
                && verificationToken.getUser().equals(verifiedUser))) {
            model.addAttribute("pageTitle", "Verification Code");
            model.addAttribute("email", email);
            model.addAttribute("errMessage", "Mã xác thực của bạn không hợp lệ!");
            return "authenticated/verification-code-form";
        }

        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("email", email);
        model.addAttribute("token", token);
        return "authenticated/update-password";
    }

    @PostMapping("/auth/save-update-password")
    public String updatePassword(
            @RequestParam("email") String email,
            @RequestParam("password") String newPassword,
            RedirectAttributes redirectAttributes) {
        User savedUser = this.userService.findUserByUserName(email);
        if (savedUser != null) {
            savedUser.setPassword(passwordEncoder.encode(newPassword));
            this.tokenService.deleteToken(savedUser);
            this.userService.save(savedUser, null);
            redirectAttributes.addFlashAttribute("Message", "Đổi mật khẩu mới thành công!");
            return "redirect:/login-form";
        }
        return "redirect:/auth/forgot";
    }
}
