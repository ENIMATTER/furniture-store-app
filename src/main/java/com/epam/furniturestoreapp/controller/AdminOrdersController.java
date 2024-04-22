package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.OrderTable;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.OrderTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.epam.furniturestoreapp.util.StaticVariables.thActionForAllProducts;

@Controller
@RequestMapping("/orders-admin")
public class AdminOrdersController {
    private final OrderTableService orderTableService;
    private final CategoryService categoryService;

    @Autowired
    public AdminOrdersController(OrderTableService orderTableService, CategoryService categoryService) {
        this.orderTableService = orderTableService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getOrdersAdmin(Model model){
        List<Category> categories = categoryService.findAll();
        List<OrderTable> orders = orderTableService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("orders", orders);
        model.addAttribute("thAction", thActionForAllProducts);
        return "orders-admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrderAdmin(@PathVariable Long id){
        orderTableService.deleteById(id);
        return "redirect:/orders-admin";
    }
}
