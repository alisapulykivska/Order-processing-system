package com.kafka.productmicroservice.service;

import com.kafka.core.command.CreateOrderCommand;
import com.kafka.core.dto.OrderItemDto;
import com.kafka.core.entity.ReservedProduct;
import com.kafka.productmicroservice.entity.Cart;
import com.kafka.productmicroservice.entity.CartItem;
import com.kafka.productmicroservice.entity.Product;
import com.kafka.productmicroservice.repository.CartRepository;
import com.kafka.productmicroservice.repository.ProductRepository;
import com.kafka.productmicroservice.service.dto.AddToCartDto;
import com.kafka.productmicroservice.service.dto.CheckoutDto;
import com.kafka.productmicroservice.service.dto.CreateProductDto;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public String createProductAsync(CreateProductDto createProductDto) {
        Product product = new Product().builder()
                .title(createProductDto.getTitle())
                .price(createProductDto.getPrice())
                .quantity(createProductDto.getQuantity())
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

    @Override
    public ReservedProduct reserve(Product desiredProduct, Long orderId) {
        // todo: reservation system
        Product productEntity = productRepository.findById(desiredProduct.getId()).orElseThrow();
        if (desiredProduct.getQuantity() > productEntity.getQuantity()) {
            //throw new ProductInsufficientQuantityException(productEntity.getId(), orderId);
            throw new RuntimeException(productEntity.getId() + "–" + orderId);
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
        Product product = productRepository.findById(addToCartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(addToCartDto.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(addToCartDto.getUserId());
                    return newCart;
                });

        // Check if product is already in cart, if so, update quantity
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + addToCartDto.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(addToCartDto.getQuantity());
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
        return addToCartDto.getUserId().toString();
    }


    @Override
    public void cancelReservation(List<ReservedProduct> productsToCancel, Long orderId) {
        for(ReservedProduct reservedProduct : productsToCancel) {
            Product product = productRepository.findById(reservedProduct.getProductId()).orElseThrow();
            product.setQuantity(product.getQuantity() + reservedProduct.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public void checkout(CheckoutDto checkoutDto) {
        Cart cart = cartRepository.findByUserId(checkoutDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        List<OrderItemDto> orderItems = cart.getCartItems().stream().map(item -> {
            Product product = item.getProduct();
            return new OrderItemDto(
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    item.getQuantity()
            );
        }).toList();

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(checkoutDto.getUserId(), orderItems);
        kafkaTemplate.send("orders-commands", createOrderCommand);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() ->
                new RuntimeException("Cart not found for user " + userId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }


}
