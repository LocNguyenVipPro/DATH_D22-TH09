package com.example.locnguyen.techzone.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.locnguyen.techzone.exceptions.TokenException;
import com.example.locnguyen.techzone.models.Token;
import com.example.locnguyen.techzone.models.User;
import com.example.locnguyen.techzone.repositories.TokenRepository;

// Service class to handle token-related operations
@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public void createToken(User user, String code) {
        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(5);
        Token token = new Token();
        token.setCreatedAt(LocalDateTime.now());
        token.setToken(code);
        token.setUser(user);
        token.setExpiresAt(expirationDateTime);
        tokenRepository.save(token);
    }

    public boolean isValidToken(Token token) {
        return token != null && !token.isExpired();
    }

    public void deleteToken(User user) {
        Token token = tokenRepository.findByUser(user);

        if (token != null) {
            this.tokenRepository.delete(token);
        }
    }

    public Token getTokenByUser(User verifiedUser) throws TokenException {
        Token verificationToken = tokenRepository.findByUser(verifiedUser);

        if (verificationToken != null) {
            return verificationToken;
        }

        throw new TokenException("Token not found for user");
    }
}
