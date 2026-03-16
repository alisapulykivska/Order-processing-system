package com.kafka.productmicroservice.service;

import com.kafka.productmicroservice.entity.Cart;
import com.kafka.productmicroservice.entity.CartItem;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.repository.CartRepository;
import com.kafka.productmicroservice.repository.ProductRepository;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public String createProductAsync(CreateProductDto createProductDto) {
        Product product = new Product().builder()
                .title(createProductDto.getTitle())
                .price(createProductDto.getPrice())
                //.quantity(createProductDto.getQuantity())
                .createdOn(LocalDateTime.now())
                .ownerId(createProductDto.getOwnerId())
                .build();

        /*
        todo: add async event notification – productCreatedEvent
        todo: create mapper or fabric for building product.
         */

        productRepository.save(product);

        return product.getId().toString();
    }

    public String addToCart(AddToCartDto addToCartDto) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(addToCartDto.getUserId());
        Product product = productRepository.findById(addToCartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // todo: check if item is already in cart. If so -> add quantity.

        if (optionalCart.isPresent()) {
            // if cart it present –> add additional cartItem to the cart
            Cart cart = optionalCart.get();

            List<CartItem> cartItems = cart.getCartItems();
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(addToCartDto.getQuantity());
            cartItems.add(newCartItem);

            cartRepository.save(cart);
        } else {
            // else -> create new cart and add cartItem to it
            Cart cart = new Cart();
            cart.setUserId(addToCartDto.getUserId());

            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(addToCartDto.getQuantity());

            cart.setCartItems(List.of(newCartItem));

            cartRepository.save(cart);
        }
        return addToCartDto.getUserId().toString();
    }
}
