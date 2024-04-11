package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
public class PayCheckoutOrderController {
    private final AddressService addressService;
    private final UserTableService userTableService;
    private final CartItemService cartItemService;
    private final OrderTableService orderTableService;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    private final List<Category> categories;

    @Autowired
    public PayCheckoutOrderController(AddressService addressService, UserTableService userTableService,
                                      CartItemService cartItemService, OrderTableService orderTableService,
                                      OrderItemService orderItemService, ProductService productService,
                                      CategoryService categoryService) {
        this.addressService = addressService;
        this.userTableService = userTableService;
        this.cartItemService = cartItemService;
        this.orderTableService = orderTableService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        categories = categoryService.findAll();
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        Address address = addressService.getByUserTableID(user);
        List<CartItem> cartItems = cartItemService.getAllItemsByUser(user);

        double sumCartItems = 0;
        for (CartItem cartItem : cartItems) {
            sumCartItems += cartItem.getProductID().getPrice() * cartItem.getQuantity();
        }

        BigDecimal shippingFee = BigDecimal.valueOf(sumCartItems * 0.1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = shippingFee.add(BigDecimal.valueOf(sumCartItems)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sumCartItemsBigDecimal = BigDecimal.valueOf(sumCartItems).setScale(2, RoundingMode.HALF_UP);

        model.addAttribute("total", total);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("sumCartItems", sumCartItemsBigDecimal);
        model.addAttribute("address", address);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "checkout";
    }

    @GetMapping("/pay")
    public String getPay(@RequestParam("totalToPay") Double totalToPay,
                         Model model) {
        model.addAttribute("totalToPay", totalToPay);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "pay";
    }

    @PostMapping("/pay")
    public String postPayAndFormOrder(@RequestParam("totalToPay") Double totalToPay,
                                      @RequestParam("cardNumber") String cardNumber,
                                      @RequestParam("expiryDate") String expiryDate,
                                      @RequestParam("CVV") String CVV) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> userCartItems = cartItemService.getAllItemsByUser(user);

        if (cardNumber == null || expiryDate == null || CVV == null || totalToPay == null) {
            return "redirect:/error-page";
        }
        if (totalToPay > user.getBalance()) {
            return "redirect:/error-page";
        }
        for(CartItem cartItem : userCartItems){
            if(cartItem.getProductID().getStockQuantity() < cartItem.getQuantity()){
                return "redirect:/error-page";
            }
        }

        user.setBalance(user.getBalance() - totalToPay);
        userTableService.editUser(user);

        OrderTable orderTable = new OrderTable();
        orderTable.setUserTableID(user);
        orderTable.setOrderDate(LocalDateTime.now());
        orderTable.setTotalAmount(totalToPay);
        orderTableService.addOrder(orderTable);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : userCartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderTableID(orderTable);
            orderItem.setProductID(cartItem.getProductID());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        orderItemService.addAllOrderItems(orderItems);

        List<Product> productsFromCart = new ArrayList<>();
        for(CartItem cartItem : userCartItems){
            Product product = cartItem.getProductID();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productsFromCart.add(product);
        }
        productService.saveAll(productsFromCart);

        cartItemService.deleteAll(userCartItems);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<OrderTable> orderTables = orderTableService.getAllByUser(user);
        Map<OrderTable, List<OrderItem>> orders = new LinkedHashMap<>();
        for (OrderTable orderTable : orderTables) {
            orders.put(orderTable, orderItemService.getAllByOrderTable(orderTable));
        }
        model.addAttribute("orders", orders);
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", thActionForAllProducts);
        return "orders";
    }
}