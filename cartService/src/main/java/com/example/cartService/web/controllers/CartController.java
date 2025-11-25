package com.example.cartService.web.controllers;



import com.example.cartService.domain.services.CartService;
import com.example.cartService.web.models.AddToCartRequest;
import com.example.cartService.web.models.CartResponse;
import com.example.cartService.web.models.UpdateCartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // ---------------------- GET CART ----------------------

    @Operation(
            summary = "Get User Cart",
            description = "Fetches the cart of a specific user from Redis or Database if cache is empty."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getUserCart(@PathVariable Long userId) {
        CartResponse response = cartService.getUserCart(userId);
        return ResponseEntity.ok(response);
    }

    // ---------------------- ADD TO CART ----------------------

    @Operation(
            summary = "Add Product to Cart",
            description = "Adds or updates a product inside the user's cart. Updates Redis and syncs to Database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added to cart"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request
    ) {
        CartResponse response = cartService.addToCart(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ---------------------- UPDATE CART ITEM ----------------------

    @Operation(
            summary = "Update Cart Item Quantity",
            description = "Updates the quantity of a specific product in the cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}/update")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        CartResponse response = cartService.updateCartItem(userId, request);
        return ResponseEntity.ok(response);
    }

    // ---------------------- REMOVE ITEM ----------------------

    @Operation(
            summary = "Remove item from cart",
            description = "Removes a specific product from the user's cart and syncs removal to DB."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid productId",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<CartResponse> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        CartResponse response = cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok(response);
    }

    // ---------------------- CLEAR CART ----------------------

    @Operation(
            summary = "Clear user cart",
            description = "Removes all items from the user's cart in both Redis and JPA database."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart cleared"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }
}
