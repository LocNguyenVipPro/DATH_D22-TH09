package com.example.dath.eshop.services;

import com.example.dath.eshop.exceptions.UserException;
import com.example.dath.eshop.models.User;
import com.example.dath.eshop.utilities.EmailSender;
import com.example.dath.eshop.utilities.GenerateRandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    private final String GMAIL = "kucantscute@gmail.com";
    private final String PASSWORD = "ugop edsx ieoo fecs";

    public void sendEmail(String email) throws UserException {
        User accountForgot=this.userService.findUserByUserName(email);
        // Password exist
        if(accountForgot!=null) {
            //Send email
            String token= GenerateRandomNumber.generateRandomNumberString();
            String to = email;
            String subject ="Your Verification Code";
            String content = "Hello : "  + accountForgot.getFullName() + ", here is your verification code: " + token;
            EmailSender.sendEmail(GMAIL,PASSWORD,to,subject,content);

            //delete old token and generate new token
            tokenService.deleteToken(accountForgot);
            tokenService.createToken(accountForgot,token);
        }else
        {
            throw new UserException("Cannot Found User With Email : "+email );
        }
    }

}
