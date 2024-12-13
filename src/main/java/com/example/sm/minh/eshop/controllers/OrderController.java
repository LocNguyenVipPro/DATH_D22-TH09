    package com.example.sm.minh.eshop.controllers;

    import com.example.sm.minh.eshop.exceptions.CartLineItemException;
    import com.example.sm.minh.eshop.exceptions.ProductException;
    import com.example.sm.minh.eshop.models.Order;
    import com.example.sm.minh.eshop.models.OrderLineItem;
    import com.example.sm.minh.eshop.models.User;
    import com.example.sm.minh.eshop.exceptions.OrderLineItemException;
    import com.example.sm.minh.eshop.exceptions.UserException;
    import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
    import com.example.sm.minh.eshop.services.OrderService;
    import com.example.sm.minh.eshop.services.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.List;

    @Controller
    public class OrderController {
        @Autowired
        private OrderService orderService;

        @Autowired
        private UserService userService;

        @GetMapping("/order/success")
        public String messageSuccess(RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("Message", "Congratulations on your successful purchase");
            return "redirect:/main-page";
        }

        @GetMapping("/order/detail")
        public String viewOrderDetail(@RequestParam("oder-line-item-id")Integer orderLineItemId,Model model, RedirectAttributes redirectAttributes) throws OrderLineItemException {
            try
            {
                model.addAttribute("pageTitle", "Order ID | "+orderLineItemId);
                model.addAttribute("viewOderLineItem",this.orderService.findOrderLineItemById(orderLineItemId));
                return "order/order-detail";
            }catch (OrderLineItemException ex)
            {
                redirectAttributes.addFlashAttribute("errorMessage","Cannot Find Order With Id "+orderLineItemId);
                return "redirect:/order/history/1";
            }
        }

        @PostMapping("/order/purchase-in-cart")
        public String purchaseProductFromCart(
                @AuthenticationPrincipal ShopMeUserDetail customer,
                @RequestParam(value = "cartLineItemId",required = false) Integer cartLineItemId,
                @RequestParam(value = "quantity", required = false) Integer quantity,
                RedirectAttributes redirectAttributes) {
            try {
                User customerUser = this.userService.findUserById(customer.getUserId());
                this.orderService.purchaseFromCart(cartLineItemId, quantity, customerUser);

            } catch (CartLineItemException |UserException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/cart/shopping-cart";
            }
            redirectAttributes.addFlashAttribute("Message", "Congratulation! You're Buy Successfully");
            return "redirect:/main-page";
        }

        @PostMapping("/order/purchase")
        public String purchaseProducts(RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal ShopMeUserDetail customer,
                                       @RequestParam(value = "productId", required = false) Integer productId,
                                       @RequestParam(value = "quantity", required = false) Integer quantity) {
            try {
                User customerUser = userService.findUserById(customer.getUserId());
                orderService.purchaseProductDirect(productId, quantity, customerUser);
                redirectAttributes.addFlashAttribute("Message", "Congratulations on your successful purchase!");
            } catch (CartLineItemException | UserException | ProductException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
            return "redirect:/main-page";
        }

        @GetMapping("/order/history/{pageNum}")
        public String viewPurchaseHistory(@PathVariable("pageNum") Integer pageNum, Model model, @AuthenticationPrincipal ShopMeUserDetail user) throws UserException {
            User customerLogin = this.userService.findUserById(user.getUserId());
            Order order = this.orderService.getOrderById(customerLogin);
            Page<OrderLineItem> listOrderLineItemPage = this.orderService.findByOrderId(order, 0);
            int totalPage = listOrderLineItemPage.getTotalPages();

            if (pageNum > totalPage) {
                pageNum = totalPage;
            }
            if (pageNum < 1) {
                pageNum = 1;
            }

            Page<OrderLineItem> listOrderLineItem = this.orderService.findByOrderId(order, pageNum - 1);
            model.addAttribute("pageTitle", "Order");
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("order", order);
            model.addAttribute("totalPage", totalPage);

            model.addAttribute("listOrderLineItem", listOrderLineItem.getContent());

            return "order/transaction";
        }
        @PostMapping("/cart/checkout")
        public String checkOutCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                   @RequestParam("productIds") List<String> productIds,
                                   @RequestParam("quantities") List<String> quantities,
                                   RedirectAttributes redirectAttributes) throws ProductException
        {
            try {
                User user = this.userService.findUserById(customer.getUserId());
                this.orderService.checkOutCart(user, productIds, quantities);
                redirectAttributes.addFlashAttribute("Message", "Congratulation! You're Buy Successfully");
                return "redirect:/main-page";
            } catch (ProductException | UserException ex) {
                return "redirect:/main-page";
            } catch (CartLineItemException e) {
                throw new RuntimeException(e);
            }
        }


    }
