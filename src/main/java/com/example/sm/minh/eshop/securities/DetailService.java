package com.example.sm.minh.eshop.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.services.UserService;

@Service
public class DetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

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
