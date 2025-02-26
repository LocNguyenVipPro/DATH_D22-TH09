package com.example.locnguyen.techzone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locnguyen.techzone.models.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    public UserProfile getUserProfileByUsersId(int id);
}
