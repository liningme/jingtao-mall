package com.jt.service;

import com.jt.pojo.Cart;

import java.util.List;

public interface DubboCartService {

    List<Cart> findCartListByUserId(long userId);



    void updateCartNumById(Cart cart);

    void addCart(Cart cart);

    void deleteItemByItemId(Cart cart);
}
