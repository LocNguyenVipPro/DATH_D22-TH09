package com.example.dath.eshop.repositories;

import com.example.dath.eshop.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    public UserProfile getUserProfileByUsersId(int id);
}
