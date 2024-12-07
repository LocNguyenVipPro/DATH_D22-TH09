package com.example.dath.eshop.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dath.eshop.exception.CartLineItemException;
import com.example.dath.eshop.exception.OrderLineItemException;
import com.example.dath.eshop.exception.ProductException;
import com.example.dath.eshop.model.*;
import com.example.dath.eshop.repository.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productsRepository;

    @Autowired
    private CartService cartService;

    private static final Integer PAGE_SIZE = 5;

    // Get Order by User
    public Order getOrderByUser(User user) throws OrderLineItemException {
        return this.orderRepository
                .findByUserId(user)
                .orElseThrow(() -> new OrderLineItemException("Order not found for user"));
    }

    // Purchase from Cart
    public void purchaseFromCart(Integer cartId, Integer quantity, User customer)
            throws CartLineItemException, ProductException {
        CartLineItem cartLineItem =
                findCartLineItemById(cartId).orElseThrow(() -> new CartLineItemException("Cart Line Item not found"));

        Order order = findOrCreateOrder(customer);

        Product product = getProductById(cartLineItem.getProductId().getId());
        Float taxPerProduct = cartLineItem.getTaxTotalAmount() / cartLineItem.getQuantity();
        Float totalAmount = calculateTotalAmount(product, quantity, taxPerProduct);
        Float taxAmount = taxPerProduct * quantity;

        OrderLineItem orderLineItem = createOrderLineItem(order, product, quantity, taxAmount, totalAmount);
        updateOrderAndCart(customer, order, cartLineItem, totalAmount, taxAmount, orderLineItem);
    }

    private Optional<CartLineItem> findCartLineItemById(Integer cartId) {
        return cartLineItemRepositoty.findById(cartId);
    }

    // Tìm hoặc tạo đơn hàng mới cho khách hàng
    private Order findOrCreateOrder(User customer) {
        return orderRepository
                .findByUserId(customer)
                .orElseGet(() -> createNewOrder(customer)); // Dùng orElseGet để tạo Order nếu không tìm thấy
    }

    private Order createNewOrder(User customer) {
        Order newOrder = new Order();
        newOrder.setUserId(customer);
        newOrder.setCountItem(0);
        return newOrder;
    }

    private Product getProductById(Integer productId) throws ProductException {
        return productsRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
    }

    private Float calculateTotalAmount(Product product, Integer quantity, Float taxPerProduct) {
        return (product.getDiscountPrice() * quantity) + (taxPerProduct * quantity);
    }

    private OrderLineItem createOrderLineItem(
            Order order, Product product, Integer quantity, Float taxAmount, Float totalAmount) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order);
        orderLineItem.setProductId(product);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setTaxTotalAmount(taxAmount);
        orderLineItem.setTotalAmount(totalAmount);
        orderLineItem.setSubTotalAmount(product.getDiscountPrice() * quantity);
        return orderLineItem;
    }

    private void updateOrderAndCart(
            User customer,
            Order order,
            CartLineItem cartLineItem,
            Float totalAmount,
            Float taxAmount,
            OrderLineItem orderLineItem)
            throws CartLineItemException {
        order.setCountItem(order.getCountItem() + cartLineItem.getQuantity());
        setDataForOrder(order, totalAmount, taxAmount);
        Cart updatedCart = setDataForCart(customer, cartLineItem);
        cartLineItem.setCartId(null); // Remove the item from the cart
        cartLineItemRepositoty.save(cartLineItem); // Save updated cart line item
        cartRepository.save(updatedCart); // Save the updated cart
        cartLineItemRepositoty.delete(cartLineItem); // Remove the cart line item
        orderLineItemRepository.save(orderLineItem); // Save the order line item
    }

    public Cart setDataForCart(User customer, CartLineItem cartLineItem) throws CartLineItemException {
        Cart cart = cartRepository
                .findByUserId(customer.getId())
                .orElseThrow(() -> new CartLineItemException("Cart not found"));

        cart.setTotalAmount(cart.getTotalAmount() - cartLineItem.getTotalAmount());
        cart.setTaxAmount(cart.getTaxAmount() - cartLineItem.getTaxTotalAmount());
        cart.setCountItem(cart.getCountItem() - cartLineItem.getQuantity());

        if (cart.getCountItem() == 0) {
            cart.setDeletedAt(new Date()); // Mark the cart as deleted if empty
        }

        return cart;
    }

    public Order setDataForOrder(Order order, Float totalAmount, Float taxAmount) {
        order.setTotalAmount(order.getTotalAmount() + totalAmount);
        order.setTaxAmount(order.getTaxAmount() + taxAmount);
        order.setUpdatedAt(new Date());
        order.setStatus(true); // Mark as completed
        return orderRepository.save(order);
    }

    // Purchase Product directly (not from Cart)
    public void purchaseProductDirect(Integer productId, Integer quantity, User customer)
            throws ProductException, CartLineItemException {
        Product product = getProductById(productId);
        Order order = findOrCreateOrder(customer);

        Float taxPerProduct = (product.getDiscountPrice() * product.getTax()) / 100;
        Float totalAmount = (product.getDiscountPrice() * quantity) + (taxPerProduct * quantity);
        Float taxAmount = taxPerProduct * quantity;

        OrderLineItem orderLineItem = createOrderLineItem(order, product, quantity, taxAmount, totalAmount);
        updateOrderAndCart(customer, order, null, totalAmount, taxAmount, orderLineItem);
    }

    public void checkOutCart(User customer, List<String> productIds, List<String> quantities)
            throws ProductException, CartLineItemException {
        int index = 0;
        for (String productId : productIds) {
            Integer quantity = Integer.parseInt(quantities.get(index++).replace(".0", ""));
            purchaseProductDirect(Integer.parseInt(productId), quantity, customer);
        }
        cartService.clearCard(customer); // Clear the cart after checkout
    }

    // Paginate Order Line Items
    public Page<OrderLineItem> findOrderLineItemsByOrderId(Order order, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return orderLineItemRepository.findByOrderId(order, pageable);
    }

    public OrderLineItem findOrderLineItemById(Integer orderLineItemId) throws OrderLineItemException {
        return orderLineItemRepository
                .findById(orderLineItemId)
                .orElseThrow(
                        () -> new OrderLineItemException("Cannot find Order Line Item with ID " + orderLineItemId));
    }

    public Order getOrderById(User user) {
        // Trả về Order của người dùng hoặc null nếu không tìm thấy
        return orderRepository
                .findByUserId(user)
                .orElseThrow(() -> new RuntimeException("Order not found for user " + user.getId()));
    }

    public Page<OrderLineItem> findByOrderId(Order order, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5); // Sử dụng số trang cố định là 5
        return orderLineItemRepository.findByOrderId(order, pageable);
    }
}
