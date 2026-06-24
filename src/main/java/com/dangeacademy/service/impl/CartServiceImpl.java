package com.dangeacademy.service.impl;

import com.dangeacademy.entity.Cart;
import com.dangeacademy.entity.Course;
import com.dangeacademy.entity.User;
import com.dangeacademy.exception.ResourceNotFoundException;
import com.dangeacademy.repository.CartRepository;
import com.dangeacademy.repository.CourseRepository;
import com.dangeacademy.repository.UserRepository;
import com.dangeacademy.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public Cart createCart(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + studentId));

        Cart cart = Cart.builder()
                .student(student)
                .build();

        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long cartId) {

        return cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found with id: " + cartId));
    }

    @Override
    public Cart getCartByStudentId(Long studentId) {

        return cartRepository.findByStudentId(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found for student id: " + studentId));
    }

    @Override
    public Cart addCourseToCart(Long cartId, Long courseId) {

        Cart cart = getCartById(cartId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!cart.getCourses().contains(course)) {
            cart.getCourses().add(course);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeCourseFromCart(Long cartId, Long courseId) {

        Cart cart = getCartById(cartId);

        cart.getCourses().removeIf(course ->
                course.getId().equals(courseId));

        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {

        Cart cart = getCartById(cartId);

        cart.getCourses().clear();

        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long cartId) {

        Cart cart = getCartById(cartId);

        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> getAllCarts() {

        return cartRepository.findAll();
    }
}