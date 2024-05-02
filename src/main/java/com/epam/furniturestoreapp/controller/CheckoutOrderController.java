package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.*;
import com.epam.furniturestoreapp.model.AddressDto;
import com.epam.furniturestoreapp.model.OrderDto;
import com.epam.furniturestoreapp.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;

@Controller
public class CheckoutOrderController {
    private final AddressService addressService;
    private final UserTableService userTableService;
    private final CartItemService cartItemService;
    private final OrderTableService orderTableService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public CheckoutOrderController(AddressService addressService, UserTableService userTableService,
                                   CartItemService cartItemService, OrderTableService orderTableService,
                                   OrderItemService orderItemService, ProductService productService,
                                   CategoryService categoryService) {
        this.addressService = addressService;
        this.userTableService = userTableService;
        this.cartItemService = cartItemService;
        this.orderTableService = orderTableService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        addToCheckoutModelBasicAttributes(model);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkoutAndFormOrder(@Valid @ModelAttribute("addressDto") AddressDto addressDto,
                                       @RequestParam("totalToPay") Double totalToPay, BindingResult result,
                                       Model model) {
        if (result.hasErrors()) {
            addToCheckoutModelBasicAttributes(model);
            model.addAttribute("fail", true);
            return "checkout";
        }

        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> userCartItems = cartItemService.getAllItemsByUser(user);

        if (totalToPay > user.getBalance()) {
            addToCheckoutModelBasicAttributes(model);
            model.addAttribute("lowbalance", true);
            return "checkout";
        }

        user.setBalance(user.getBalance() - totalToPay);
        userTableService.editUser(user);

        OrderTable orderTable = new OrderTable();
        orderTable.setUserTableID(user);
        orderTable.setOrderDate(LocalDateTime.now());
        orderTable.setTotalAmount(totalToPay);
        orderTableService.addOrder(orderTable);

        Address address = new Address();
        address.setStreet(addressDto.getStreet());
        address.setHouse(addressDto.getHouse());
        address.setFloor(addressDto.getFloor());
        address.setApartment(addressDto.getApartment());
        address.setMessage(addressDto.getMessage());
        address.setOrderTableID(orderTable);
        addressService.addAddress(address);

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
        for (CartItem cartItem : userCartItems) {
            Product product = cartItem.getProductID();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productsFromCart.add(product);
        }
        productService.saveAll(productsFromCart);

        cartItemService.deleteAll(userCartItems);

        addToAccountModelBasicAttributes(model);
        return "account";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        addToOrderModelBasicAttributes(model);
        return "orders";
    }

    private void addToCheckoutModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);

        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<CartItem> cartItems = cartItemService.getAllItemsByUser(user);

        double sumCartItems = getSumCartItems(cartItems);
        BigDecimal shippingFee = BigDecimal.valueOf(sumCartItems * 0.1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = shippingFee.add(BigDecimal.valueOf(sumCartItems)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sumCartItemsBigDecimal = BigDecimal.valueOf(sumCartItems).setScale(2, RoundingMode.HALF_UP);

        model.addAttribute("total", total);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("sumCartItems", sumCartItemsBigDecimal);
    }

    private void addToOrderModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);

        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        List<OrderTable> orders = orderTableService.getAllByUser(user);
        List<OrderDto> orderDtos = new ArrayList<>();
        for(OrderTable order : orders){
            orderDtos.add(new OrderDto(order));
        }
        Collections.reverse(orderDtos);
        model.addAttribute("orderDtos", orderDtos);
    }

    private void addToAccountModelBasicAttributes(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
        String emailUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserTable user = userTableService.getUserByEmail(emailUsername);
        double balance = user.getBalance();
        model.addAttribute("balance", balance);
    }

    private double getSumCartItems(List<CartItem> cartItems) {
        double sumCartItems = 0;
        for (CartItem cartItem : cartItems) {
            sumCartItems += cartItem.getProductID().getPrice() * cartItem.getQuantity();
        }
        return sumCartItems;
    }
}