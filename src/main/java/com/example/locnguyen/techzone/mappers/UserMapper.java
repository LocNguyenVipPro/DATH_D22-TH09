package com.example.locnguyen.techzone.mappers;

import com.example.locnguyen.techzone.models.User;
import com.example.locnguyen.techzone.requests.UserRequest;

public class UserMapper {
    public static User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(userRequest.getPassword());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setIsActive(userRequest.getIsActive());
        user.setId(userRequest.getId());
        return user;
    }

    public static UserRequest toUserRequest(User user) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName(user.getUserName());
        userRequest.setPassword(user.getPassword());
        userRequest.setFirstName(user.getFirstName());
        userRequest.setLastName(user.getLastName());
        userRequest.setIsActive(user.getIsActive());
        userRequest.setId(user.getId());
        return userRequest;
    }
}
