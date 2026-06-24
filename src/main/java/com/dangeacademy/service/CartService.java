package com.dangeacademy.service;

import com.dangeacademy.entity.Cart;

import java.util.List;

public interface CartService {

    Cart createCart(Long studentId);

    Cart getCartById(Long cartId);

    Cart getCartByStudentId(Long studentId);

    Cart addCourseToCart(Long cartId, Long courseId);

    Cart removeCourseFromCart(Long cartId, Long courseId);

    void clearCart(Long cartId);

    void deleteCart(Long cartId);

    List<Cart> getAllCarts();
}