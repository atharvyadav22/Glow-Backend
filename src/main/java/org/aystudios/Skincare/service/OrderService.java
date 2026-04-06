package org.aystudios.Skincare.service;

import org.aystudios.Skincare.dto.*;
import org.aystudios.Skincare.entity.*;
import org.aystudios.Skincare.exception.auth.UserNotFoundException;
import org.aystudios.Skincare.exception.product.ProductNotFoundException;
import org.aystudios.Skincare.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public OrderService(CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(String email, OrderRequestDTO dto) {

        // Step 1: Fetch User by email
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        Long userId = userEntity.getId();

        // Step 2: Get Cart Items of user
        List<CartItemEntity> cartItems = cartRepository.findByUserId(userId);

        // If cart is empty, don't allow order creation
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Step 3: Create Order (initially without total price)
        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setPaymentMode(dto.getPaymentMode());
        order.setCreatedAt(LocalDateTime.now());

        // If Order Is COD Directly marked as confirmed
        if(dto.getPaymentMode() == PaymentMode.COD){
            order.setStatus(OrderStatus.CONFIRMED);
        }
        else order.setStatus(OrderStatus.CREATED);

        order.setPaymentStatus(PaymentStatus.INITIATED);


        // Save order first to generate orderId
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Step 4: Loop through cart items and convert to OrderItems
        for (CartItemEntity item : cartItems) {


            ProductEntity productEntity = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(ProductNotFoundException::new);

            // Step 5: Check stock availability
            if (productEntity.getQuantity() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient quantity for product: " + productEntity.getName());
            }

            // Step 6: Reduce product stock
            productEntity.setQuantity(
                    productEntity.getQuantity() - item.getQuantity()
            );
            productRepository.save(productEntity);

            // Step 7: Calculate item total price
            BigDecimal itemTotal = productEntity.getDiscountPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);

            // Step 8: Create OrderItem (store snapshot of product info)
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(productEntity.getId());
            orderItem.setProductName(productEntity.getName());
            orderItem.setBrand(productEntity.getBrand());
            orderItem.setImage(productEntity.getImage());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(productEntity.getDiscountPrice());

            orderItemRepository.save(orderItem);
        }

        // Step 9: Set final total price in Order
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // Step 10: Clear cart after successful order creation
        cartRepository.deleteAll(cartItems);


        return new OrderResponseDTO(
                order.getOrderId(),
                order.getStatus(),
                dto.getPaymentMode(),
                totalPrice
        );
    }

    @Transactional(readOnly = true)
    public List<MyOrderResponseDTO> getMyOrders(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return orderRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(order -> {

                    List<OrderItemResponseDTO> items =
                            orderItemRepository.findByOrderId(order.getOrderId())
                                    .stream()
                                    .map(item -> new OrderItemResponseDTO(
                                            item.getProductId(),
                                            item.getProductName(),
                                            item.getBrand(),
                                            item.getImage(),
                                            item.getQuantity(),
                                            item.getPriceAtPurchase()
                                    ))
                                    .toList();

                    return new MyOrderResponseDTO(
                            order.getOrderId(),
                            order.getStatus(),
                            order.getPaymentMode(),
                            order.getTotalPrice(),
                            order.getCreatedAt(),
                            items
                    );
                })
                .toList();
    }

    @Transactional
    public Void deleteAllOrders() {
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        return null;
    }

    @Transactional
    public String deleteOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "Successfully deleted order with id: " + orderId;
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {

        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return new OrderResponseDTO(order.getOrderId(), order.getStatus(), order.getPaymentMode(), order.getTotalPrice());
    }

}
