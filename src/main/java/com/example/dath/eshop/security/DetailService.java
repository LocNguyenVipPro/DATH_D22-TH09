package com.example.dath.eshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.dath.eshop.model.User;
import com.example.dath.eshop.service.UserService;

@Service
public class DetailService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public DetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Cannot Find User With UserName " + username);
        } else {
            return new ShopMeUserDetail(user);
        }
    }
}
