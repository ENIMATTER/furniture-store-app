package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Image;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ImageService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import com.epam.furniturestoreapp.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.epam.furniturestoreapp.model.StaticVariables.*;

@Controller
@RequestMapping("/products-admin")
public class AdminProductsController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @Autowired
    public AdminProductsController(ProductService productService, CategoryService categoryService,
                                   ImageService imageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @GetMapping
    public String getProductsAdmin(Model model){
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products", products);
        addToModelBasicAttributes(model, categories);
        return "products-admin";
    }

    @GetMapping("/add")
    public String getAddProductAdmin(Model model){
        List<Category> categories = categoryService.findAll();
        addToModelBasicAttributes(model, categories);
        model.addAttribute("materialMap", null);
        return "products-admin-add-edit";
    }

    @PostMapping("/add")
    public String postAddProductAdmin(@ModelAttribute("productUtil") ProductDto productUtil){
        StringBuilder material = new StringBuilder();
        Material[] materials = productUtil.getMaterials();
        for(int i = 0; i < materials.length; i++){
            material.append(materials[i]);
            if(i != materials.length - 1){
                material.append(",");
            }
        }

        String color = productUtil.getColor().name();

        Category category = categoryService.findByName(productUtil.getCategoryName());

        Double averageRating = 5.0;

        Product product = new Product(productUtil.getProductName(), productUtil.getProductDescription(),
                category, productUtil.getPrice(), productUtil.getStockQuantity(),
                productUtil.getDimensions(), material.toString(), color, averageRating);

        productService.save(product);

        Image image = new Image(product, productUtil.getImagePath());
        imageService.save(image);

        return "redirect:/products-admin";
    }

    @GetMapping("/edit/{id}")
    public String getEditProductAdmin(@PathVariable Long id, Model model){
        Product product = productService.getProductById(id);
        String[] materialsStr = product.getMaterial().split(",");

        Material[] materials = new Material[materialsStr.length];
        int i = 0;
        for(String s : materialsStr){
            for(Material m : MATERIAL_LIST){
                if(s.equals(m.name())) {
                    materials[i] = m;
                    i++;
                }
            }
        }

        boolean[] checked = new boolean[MATERIAL_LIST.size()];
        int j = 0;
        for(Material m1 : MATERIAL_LIST){
            for(Material m2 : materials){
                if(m1.equals(m2)){
                    checked[j] = true;
                    break;
                } else {
                    checked[j] = false;
                }
            }
            j++;
        }
        Map<Material, Boolean> materialMap = new LinkedHashMap<>();
        for(int k = 0; k < MATERIAL_LIST.size(); k++){
            materialMap.put(MATERIAL_LIST.get(k), checked[k]);
        }

        Color color = null;
        for(Color c : COLOR_MAP.keySet()){
            if(c.name().equals(product.getColor())){
                color = c;
            }
        }

        ProductDto productUtil;

        if(product.getCategoryID() != null){
            productUtil = new ProductDto(product.getProductName(), product.getProductDescription(),
                    product.getCategoryID().getCategoryName(), product.getPrice(), product.getStockQuantity(),
                    product.getDimensions(), materials, color, product.getImage().getImagePath());
        } else {
            productUtil = new ProductDto(product.getProductName(), product.getProductDescription(),
                    null, product.getPrice(), product.getStockQuantity(),
                    product.getDimensions(), materials, color, product.getImage().getImagePath());
        }

        List<Category> categories = categoryService.findAll();
        if(product.getCategoryID() != null){
            categories.remove(product.getCategoryID());
            categories.add(0, product.getCategoryID());
        }

        addToModelBasicAttributes(model, categories);
        model.addAttribute("productID", id);
        model.addAttribute("materialMap", materialMap);
        model.addAttribute("productUtil", productUtil);
        return "products-admin-add-edit";
    }

    @PutMapping("/edit/{id}")
    public String putEditProductAdmin(@PathVariable Long id,
                                      @ModelAttribute("productUtil") ProductDto productUtil){
        Product product = productService.getProductById(id);
        Category category = categoryService.findByName(productUtil.getCategoryName());
        String material = Arrays.toString(productUtil.getMaterials())
                .replace(" ", "").replace("[", "").replace("]", "");
        String color = productUtil.getColor().name();

        product.setProductName(productUtil.getProductName());
        product.setProductDescription(productUtil.getProductDescription());
        product.setCategoryID(category);
        product.setPrice(productUtil.getPrice());
        product.setStockQuantity(productUtil.getStockQuantity());
        product.setDimensions(productUtil.getDimensions());
        product.setMaterial(material);
        product.setColor(color);

        Image image = product.getImage();
        image.setImagePath(productUtil.getImagePath());

        productService.save(product);
        imageService.save(image);

        return "redirect:/products-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteProductAdmin(@PathVariable Long id){
        if(productService.existsById(id)){
            productService.deleteById(id);
        }
        return "redirect:/products-admin";
    }

    private void addToModelBasicAttributes(Model model, List<Category> categories) {
        model.addAttribute("categories", categories);
        model.addAttribute("filterList", FILTER_LIST);
        model.addAttribute("colorMap", COLOR_MAP);
        model.addAttribute("materialList", MATERIAL_LIST);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }
}
