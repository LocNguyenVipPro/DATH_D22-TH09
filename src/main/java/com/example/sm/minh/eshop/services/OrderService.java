package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.models.*;
import com.example.sm.minh.eshop.exceptions.CartLineItemException;
import com.example.sm.minh.eshop.exceptions.OrderLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.CartReposttory;
import com.example.sm.minh.eshop.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartReposttory cartReposttory;

    @Autowired
    private ProductRepository productsRepository;
    @Autowired
    private CartService cartService;

    private static Integer PAGE_SIZE=5;

    public Order getOrderById(User users)
    {
        return this.orderRepository.findByUserId(users);
    }

    // This method is used when the user wants to purchase from their shopping cart.
    public void purchaseFromCart(Integer cartId, Integer quantity, User customer) throws CartLineItemException {
        Optional<CartLineItem> cartOptional = findCartLineItemById(cartId);
        CartLineItem cartLineItemPayment=cartOptional.orElseThrow(()-> new CartLineItemException("Cannot Found Cart"));
        Order order = findOrCreateOrder(customer);

        //Get product and calculator
        Product product = getProductById(cartLineItemPayment.getProductId().getId());
        Float taxPerProduct = cartLineItemPayment.getTaxTotalAmount() / cartLineItemPayment.getQuantity();
        Float totalAmount = calculateTotalAmount(product, quantity, taxPerProduct);
        Float taxAmount = taxPerProduct * quantity;

        OrderLineItem orderLineItem = createOrderLineItem(order, product, quantity, taxAmount, totalAmount);
        updateOrderAndCart(customer, order, cartLineItemPayment, totalAmount, taxAmount, orderLineItem);
    }

    private Optional<CartLineItem> findCartLineItemById(Integer cartId) {
        return this.cartLineItemRepositoty.findById(cartId);
    }

    private Order findOrCreateOrder(User customer) {
        Order order = this.orderRepository.findByUserId(customer);
        if (order == null) {
            order = new Order();
            order.setUserId(customer);
            order.setCountItem(0);
        }
        return order;
    }

    private Product getProductById(Integer productId) {
        return this.productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private Float calculateTotalAmount(Product product, Integer quantity, Float taxPerProduct) {
        return (product.getDiscountPrice() * quantity) + (taxPerProduct * quantity);
    }

    //create order and setup data
    private OrderLineItem createOrderLineItem(Order order, Product product, int quantity, Float taxAmount, Float totalAmount) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order);
        orderLineItem.setProductId(product);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setTaxTotalAmount(taxAmount);
        orderLineItem.setTotalAmount(totalAmount);
        orderLineItem.setSubTotalAmount(product.getDiscountPrice() * quantity);
        return orderLineItem;
    }

    private void updateOrderAndCart(User customer, Order order, CartLineItem cartLineItemPayment, Float totalAmount, Float taxAmount, OrderLineItem orderLineItem) {
        order.setCountItem(order.getCountItem() + cartLineItemPayment.getQuantity());
        setDataForOrder(order, totalAmount, taxAmount);
        Cart updateCart = setDataForCart(customer, cartLineItemPayment);
        cartLineItemPayment.setCartId(null);
        this.cartLineItemRepositoty.save(cartLineItemPayment);
        this.cartReposttory.save(updateCart);
        this.cartLineItemRepositoty.delete(cartLineItemPayment);
        this.orderLineItemRepository.save(orderLineItem);
    }


    public Cart setDataForCart(User customer, CartLineItem cartLineItemPayment)
    {
        Cart updateCart = this.cartReposttory.findByUserId(customer.getId());
        updateCart.setTotalAmount(updateCart.getTotalAmount() - cartLineItemPayment.getTotalAmount());
        updateCart.setTaxAmount(updateCart.getTaxAmount() - cartLineItemPayment.getTaxTotalAmount());
        updateCart.setCountItem(updateCart.getCountItem() - cartLineItemPayment.getQuantity());

        if (updateCart.getCountItem() == 0) {
            updateCart.setDeletedAt(new Date());
        }

        return updateCart;
    }
    public Order setDataForOrder(Order order, Float totalAmount, Float taxAmount) {
        order.setTotalAmount(order.getTotalAmount() + totalAmount);
        order.setTaxAmount(order.getTaxAmount() + taxAmount);
        order.setUpdatedAt(new Date());
        order.setStatus(true);
        return this.orderRepository.save(order);
    }

    public OrderLineItem setDataForOderLineItem(Order order, Product product, int quantity, Float taxAmount, Float totalAmount)
    {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order);
        orderLineItem.setProductId(product);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setTaxTotalAmount(taxAmount);
        orderLineItem.setTotalAmount(totalAmount);
        orderLineItem.setSubTotalAmount(product.getDiscountPrice() * quantity);
        return orderLineItem;
    }

    public void purchaseProductDirect(Integer productId, Integer quantity, User customer) throws CartLineItemException, ProductException {
        Optional<Product> selectProduct=this.productsRepository.findById(productId);

        if(selectProduct.isPresent())
        {
            Product productSelect=selectProduct.get();
            Order order = this.orderRepository.findByUserId(customer);

            if (order == null) {
                order = new Order();
                order.setUserId(customer);
                order.setCountItem(0);
            }

            order.setCountItem(order.getCountItem() + quantity);
            Float taxPerProduct=(productSelect.getDiscountPrice()/100)*productSelect.getTax();
            Float totalAmount = (productSelect.getDiscountPrice() * quantity) + (taxPerProduct * quantity);
            Float taxAmount = taxPerProduct * quantity;
            OrderLineItem orderLineItem=this.setDataForOderLineItem(order,productSelect,quantity,taxAmount,totalAmount);
            order = setDataForOrder(order, totalAmount, taxAmount);
            this.orderLineItemRepository.save(orderLineItem);

        }else
        {
            throw new ProductException("Cannot Found Product With Id "+productId);
        }

    }

    public void checkOutCart(User customer, List<String> productIds, List<String> quantities) throws ProductException, CartLineItemException {
        int index=0;

        //User Loop to delete all cart and checkout all
        for(String s : productIds)
        {
            Integer quantity=Integer.parseInt(quantities.get(index++).replace(".0",""));
            this.purchaseProductDirect(Integer.parseInt(s), quantity,customer);
            this.cartService.clearCard(customer);
        }

    }

    public Page<OrderLineItem> findByOrderId(Order order, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return this.orderLineItemRepository.findByOrderId(order,pageable);
    }

    public List<Object[]> getTotalAmountByMonthInYear() {
        Calendar cal = Calendar.getInstance();
        List<OrderLineItem> listOrderLineItem = orderLineItemRepository.findAll();

        List<Object[]> monthlyTotals = new ArrayList<>();

        for (int month = 0; month < 12; month++) {
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date startOfMonth = cal.getTime();

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endOfMonth = cal.getTime();

            Double totalAmount = 0.0;
            for (OrderLineItem item : listOrderLineItem) {
                if (!item.getCreatedAt().before(startOfMonth) && !item.getCreatedAt().after(endOfMonth)) {
                    totalAmount += item.getTotalAmount();
                }
            }

            monthlyTotals.add(new Object[] {month + 1, totalAmount});
        }

        return monthlyTotals;
    }



    public OrderLineItem findOrderLineItemById(Integer oderLineItemId) throws OrderLineItemException {
        Optional<OrderLineItem>OderLineItem=this.orderLineItemRepository.findById(oderLineItemId);

        if(OderLineItem.isPresent())
        {
            return OderLineItem.get();
        }else
        {
            throw new OrderLineItemException("Cannot Found Oder Line Item With Id "+oderLineItemId);
        }

    }
}