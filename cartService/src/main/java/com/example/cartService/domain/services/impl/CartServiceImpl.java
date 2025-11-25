package com.example.cartService.domain.services.impl;

import com.example.cartService.domain.entities.Cart;
import com.example.cartService.domain.entities.CartItem;
import com.example.cartService.domain.redis.RedisCart;
import com.example.cartService.domain.redis.RedisCartItem;
import com.example.cartService.domain.repos.CartItemRepo;
import com.example.cartService.domain.repos.CartRepo;
import com.example.cartService.domain.services.CartService;
import com.example.cartService.domain.services.mappers.CartMapper;
import com.example.cartService.web.models.AddToCartRequest;
import com.example.cartService.web.models.CartResponse;
import com.example.cartService.web.models.UpdateCartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final CartMapper cartMapper;

    private String redisKey(Long userId) {
        return "cart:" + userId;
    }

    @Override
    public CartResponse getUserCart(Long userId) {
        // 1. Try Redis
        RedisCart redisCart = (RedisCart) redisTemplate.opsForValue().get(redisKey(userId));

        if (redisCart != null)
            return cartMapper.mapRedisToResponse(redisCart);

        // 2. Load from DB
        Cart cart = cartRepository.findById(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId, 0.0, new ArrayList<>())));

        // 3. Store in Redis
        redisTemplate.opsForValue().set(redisKey(userId), cartMapper.mapDbToRedis(cart));

        return cartMapper.mapDbToResponse(cart);
    }

    @Override
    public CartResponse addToCart(Long userId, AddToCartRequest req) {

        // -----------------------------------
        // 1) Load from Redis OR fallback to DB
        // -----------------------------------
        RedisCart redisCart = (RedisCart) redisTemplate.opsForValue().get(redisKey(userId));

        if (redisCart == null) {
            // ------- Redis expired -> rebuild from DB -------
            Cart dbCart = cartRepository.findById(userId)
                    .orElseGet(() -> new Cart(userId, 0.0, new ArrayList<>()));

            redisCart = cartMapper.mapDbToRedis(dbCart); // FULL cart
        }

        // -----------------------------------
        // 2) Update Redis cart
        // -----------------------------------
        redisCart.getItems().add(
                new RedisCartItem(req.getProductId(), req.getQuantity(), req.getPrice())
        );

        redisCart.setTotalAmount(redisCart.getTotalAmount()
                + (req.getQuantity() * req.getPrice()));

        redisTemplate.opsForValue().set(redisKey(userId), redisCart);


        // -----------------------------------
        // 3) Sync to DB
        // -----------------------------------
        Cart dbCart = cartRepository.findById(userId)
                .orElseGet(() -> new Cart(userId, 0.0, new ArrayList<>()));

        CartItem dbItem = CartItem.builder()
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .price(req.getPrice())
                .cart(dbCart)
                .build();

        dbCart.getItems().add(dbItem);

        // use redis total amount as source of truth
        dbCart.setTotalAmount(redisCart.getTotalAmount());

        cartRepository.save(dbCart);


        // -----------------------------------
        // 4) return updated cart
        // -----------------------------------
        return cartMapper.mapRedisToResponse(redisCart);
    }


    @Override
    public CartResponse updateCartItem(Long userId, UpdateCartItemRequest req) {

        // 1) Fetch Redis OR rebuild from DB
        RedisCart redisCart = (RedisCart) redisTemplate.opsForValue().get(redisKey(userId));
        if (redisCart == null) {
            Cart dbCart = cartRepository.findById(userId)
                    .orElseGet(() -> new Cart(userId, 0.0, new ArrayList<>()));
            redisCart = cartMapper.mapDbToRedis(dbCart);
        }

        // 2) Update the item in Redis
        Optional<RedisCartItem> existing = redisCart.getItems().stream()
                .filter(i -> i.getProductId().equals(req.getCartItemId()))
                .findFirst();

        if (existing.isEmpty()) {
            throw new RuntimeException("Item not found in cart");
        }

        RedisCartItem item = existing.get();

        // Update totals: subtract old, add new
        redisCart.setTotalAmount(
                redisCart.getTotalAmount()
                        - (item.getQuantity() * item.getPrice())
                        + (req.getQuantity() * item.getPrice())
        );

        // Update quantity
        item.setQuantity(req.getQuantity());

        redisTemplate.opsForValue().set(redisKey(userId), redisCart);

        // 3) Sync DB
        Cart dbCart = cartRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem dbItem = dbCart.getItems().stream()
                .filter(i -> i.getProductId().equals(req.getCartItemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in DB"));

        dbItem.setQuantity(req.getQuantity());

        dbCart.setTotalAmount(redisCart.getTotalAmount());
        cartRepository.save(dbCart);

        return cartMapper.mapRedisToResponse(redisCart);
    }


    @Override
    public CartResponse removeCartItem(Long userId, Long productId) {

        // 1) Load from Redis or DB
        RedisCart redisCart = (RedisCart) redisTemplate.opsForValue().get(redisKey(userId));
        if (redisCart == null) {
            Cart dbCart = cartRepository.findById(userId)
                    .orElseGet(() -> new Cart(userId, 0.0, new ArrayList<>()));
            redisCart = cartMapper.mapDbToRedis(dbCart);
        }

        // 2) Remove from Redis
        RedisCartItem item = redisCart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        redisCart.setTotalAmount(
                redisCart.getTotalAmount() - (item.getQuantity() * item.getPrice())
        );

        redisCart.getItems().remove(item);

        redisTemplate.opsForValue().set(redisKey(userId), redisCart);

        // 3) Sync DB
        Cart dbCart = cartRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        dbCart.getItems().removeIf(i -> i.getProductId().equals(productId));
        dbCart.setTotalAmount(redisCart.getTotalAmount());

        cartRepository.save(dbCart);

        return cartMapper.mapRedisToResponse(redisCart);
    }


    @Override
    public void clearCart(Long userId) {

        // 1) Clear Redis
        redisTemplate.delete(redisKey(userId));

        // 2) Clear DB
        Cart dbCart = cartRepository.findById(userId)
                .orElse(null);

        if (dbCart != null) {
            dbCart.getItems().clear();
            dbCart.setTotalAmount(0.0);
            cartRepository.save(dbCart);
        }
    }

}
