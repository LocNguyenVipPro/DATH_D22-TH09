package com.example.locnguyen.techzone.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.locnguyen.techzone.models.WishList;
import com.example.locnguyen.techzone.repositories.WishListRepository;
import com.example.locnguyen.techzone.securities.ShopMeUserDetail;

@Service
public class WishListService {
    @Autowired
    public WishListRepository wishListRepository;

    public WishList saveWishList(WishList wishList) {
        return wishListRepository.save(wishList);
    }

    public WishList update(WishList wishList) {
        Optional<WishList> wishListOptional = this.wishListRepository.findById(wishList.getId());
        if (wishListOptional.isPresent()) {
            WishList wishListData = wishListOptional.get();
            wishListData.setUser(wishList.getUser());
            return this.wishListRepository.save(wishListData);
        } else {
            throw new IllegalArgumentException("WishList với id " + wishList.getId() + " không tồn tại");
        }
    }

    public WishList remove(ShopMeUserDetail customer, int id) {
        Optional<WishList> wishListOptional =
                Optional.ofNullable(this.wishListRepository.findByUserIdAndProductId(customer.getUserId(), id));
        wishListOptional.ifPresent(list -> this.wishListRepository.delete(list));
        return wishListOptional.orElse(null);
    }

    public List<WishList> findAll(int id) {
        return this.wishListRepository.findWishListByUserId(id);
    }
}
