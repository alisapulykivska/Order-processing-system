package com.kafka.productmicroservice.service;

import com.kafka.core.command.CreateOrderCommand;
import com.kafka.core.dto.OrderItemDto;
import com.kafka.core.entity.ReservedProduct;
import com.kafka.core.exception.product.CartItemNotFoundException;
import com.kafka.core.exception.product.CartNotFoundException;
import com.kafka.core.exception.product.InsufficientProductQuantityException;
import com.kafka.core.exception.product.ProductNotFoundException;
import com.kafka.productmicroservice.entity.Cart;
import com.kafka.productmicroservice.entity.CartItem;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.repository.CartItemRepository;
import com.kafka.productmicroservice.repository.CartRepository;
import com.kafka.productmicroservice.repository.ProductRepository;
import com.kafka.productmicroservice.service.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public String createProductAsync(CreateProductDto createProductDto) {
        Product product = new Product().builder()
                .title(createProductDto.title())
                .price(createProductDto.price())
                .quantity(createProductDto.quantity())
                .createdOn(LocalDateTime.now())
                .ownerId(createProductDto.ownerId())
                .build();

        /*
        todo: add async event notification – productCreatedEvent
        todo: create mapper or fabric for building product.
         */

        productRepository.save(product);

        return product.getId().toString();
    }

    @Override
    public ReservedProduct reserve(Product desiredProduct, Long orderId) {
        // todo: reservation system
        Product productEntity = productRepository.findById(desiredProduct.getId()).orElseThrow();
        if (desiredProduct.getQuantity() > productEntity.getQuantity()) {
            throw new InsufficientProductQuantityException(productEntity.getId(), orderId);
        }

        productEntity.setQuantity(productEntity.getQuantity() - desiredProduct.getQuantity());
        productRepository.save(productEntity);

        return new ReservedProduct(
                desiredProduct.getId(),
                productEntity.getPrice(),
                desiredProduct.getQuantity()
        );
    }

    public String addToCart(AddToCartDto addToCartDto) {
        Product product = productRepository.findById(addToCartDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(addToCartDto.productId()));

        Cart cart = cartRepository.findByUserId(addToCartDto.userId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(addToCartDto.userId());
                    return newCart;
                });

        // Check if product is already in cart, if so, update quantity
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + addToCartDto.quantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(addToCartDto.quantity());
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
        return addToCartDto.userId().toString();
    }

    @Override
    public void removeProductFromCart(RemoveFromCartDto removeFromCartDto) {
        Product product = productRepository.findById(removeFromCartDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(removeFromCartDto.productId()));

        Cart cart = cartRepository.findByUserId(removeFromCartDto.userId())
                .orElseThrow(( ) -> new CartNotFoundException(removeFromCartDto.userId()));

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            cart.getCartItems().remove(existingItemOpt.get());
            cartRepository.save(cart);
        } else {
            throw new CartItemNotFoundException(removeFromCartDto.productId(), removeFromCartDto.userId());
        }
    }

    @Override
    public void cancelReservation(List<ReservedProduct> productsToCancel, Long orderId) {
        for(ReservedProduct reservedProduct : productsToCancel) {
            Product product = productRepository.findById(reservedProduct.productId()).orElseThrow();
            product.setQuantity(product.getQuantity() + reservedProduct.quantity());
            productRepository.save(product);
        }
    }

    @Override
    public void checkout(CheckoutDto checkoutDto) {
        Cart cart = cartRepository.findByUserId(checkoutDto.userId())
                .orElseThrow(() -> new CartNotFoundException(checkoutDto.userId()));

        List<OrderItemDto> orderItems = cart.getCartItems().stream().map(item -> {
            Product product = item.getProduct();
            return new OrderItemDto(
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    item.getQuantity()
            );
        }).toList();

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(checkoutDto.userId(), orderItems);
        kafkaTemplate.send("orders-commands", createOrderCommand);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() ->
                new CartNotFoundException(userId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public Product updateProduct(Long productId, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        product.builder()
                .title(updateProductDto.title())
                .price(updateProductDto.price())
                .quantity(updateProductDto.quantity());
        return productRepository.save(product);
    }


}
