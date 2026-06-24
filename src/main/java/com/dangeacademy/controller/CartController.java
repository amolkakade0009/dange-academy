package com.dangeacademy.controller;

import com.dangeacademy.entity.Cart;
import com.dangeacademy.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Create cart for a student
    @PostMapping("/student/{studentId}")
    public ResponseEntity<Cart> createCart(@PathVariable Long studentId) {
        return ResponseEntity.ok(cartService.createCart(studentId));
    }

    // Get cart by id
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCartById(cartId));
    }

    // Get cart by student id
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Cart> getCartByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(cartService.getCartByStudentId(studentId));
    }

    // Add course to cart
    @PostMapping("/{cartId}/courses/{courseId}")
    public ResponseEntity<Cart> addCourseToCart(@PathVariable Long cartId, @PathVariable Long courseId) {
        return ResponseEntity.ok(
                cartService.addCourseToCart(cartId, courseId)
        );
    }

    // Remove course from cart
    @DeleteMapping("/{cartId}/courses/{courseId}")
    public ResponseEntity<Cart> removeCourseFromCart(@PathVariable Long cartId,@PathVariable Long courseId) {
        return ResponseEntity.ok(
                cartService.removeCourseFromCart(cartId, courseId)
        );
    }

    // Clear cart
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    // Delete cart
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {

        cartService.deleteCart(cartId);

        return ResponseEntity.ok("Cart deleted successfully");
    }

    // Get all carts
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }
}