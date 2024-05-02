package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.model.AdminProductsDto;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import com.epam.furniturestoreapp.model.ProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.epam.furniturestoreapp.model.StaticVariables.*;

@Controller
@RequestMapping("/products-admin")
public class AdminProductsController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public AdminProductsController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getProductsAdmin(Model model){
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.findAll();
        List<AdminProductsDto> productsDtos = getAdminProductsDtos(products);
        model.addAttribute("productsDtos", productsDtos);
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
    public String postAddProductAdmin(@Valid @ModelAttribute("productUtil") ProductDto productUtil,
                                      BindingResult result, Model model) throws IOException {
        List<Category> categories = categoryService.findAll();
        addToModelBasicAttributes(model, categories);
        model.addAttribute("materialMap", null);
        if (result.hasErrors()) {
            model.addAttribute("fail", true);
            return "products-admin-add-edit";
        }
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

        product.setImage(productUtil.getImage().getBytes());
        productService.save(product);

        List<Product> products = productService.getAllProducts();
        List<AdminProductsDto> productsDtos = getAdminProductsDtos(products);
        model.addAttribute("productsDtos", productsDtos);

        return "products-admin";
    }

    @GetMapping("/edit/{id}")
    public String getEditProductAdmin(@PathVariable Long id, Model model){
        Product product = productService.getProductById(id);
        String[] productMaterials = product.getMaterial().split(",");
        Material[] materials = getMaterialArrayFromStringArray(productMaterials);
        Map<Material, Boolean> materialMap = getMaterialMapForProduct(materials);

        Color color = null;
        for(Color c : COLOR_MAP.keySet()){
            if(c.name().equals(product.getColor())){
                color = c;
            }
        }

        ProductDto productUtil = new ProductDto(product.getProductName(), product.getProductDescription(),
                product.getPrice(), product.getStockQuantity(), product.getDimensions(), materials, color);

        if(product.getCategoryID() != null){
            productUtil.setCategoryName(product.getCategoryID().getCategoryName());
        } else {
            productUtil.setCategoryName(null);
        }
        String imageString = Base64.getEncoder().encodeToString(product.getImage());
        productUtil.setImageString(imageString);

        List<Category> categories = getListCategoriesForProduct(product);

        addToModelBasicAttributes(model, categories);
        model.addAttribute("productID", id);
        model.addAttribute("materialMap", materialMap);
        model.addAttribute("productUtil", productUtil);
        return "products-admin-add-edit";
    }

    @PutMapping("/edit/{id}")
    public String putEditProductAdmin(@PathVariable Long id,
                                      @Valid @ModelAttribute("productUtil") ProductDto productUtil,
                                      BindingResult result, Model model) throws IOException {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.findAll();
        addToModelBasicAttributes(model, categories);

        if (result.hasErrors()) {
            categories = getListCategoriesForProduct(product);
            String[] productMaterials = product.getMaterial().split(",");
            Material[] materials = getMaterialArrayFromStringArray(productMaterials);
            Map<Material, Boolean> materialMap = getMaterialMapForProduct(materials);

            model.addAttribute("productID", id);
            model.addAttribute("materialMap", materialMap);
            model.addAttribute("productUtil", productUtil);
            model.addAttribute("categories", categories);
            model.addAttribute("fail", true);
            return "products-admin-add-edit";
        }

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

        if(productUtil.getImage().getBytes().length != 0){
            product.setImage(productUtil.getImage().getBytes());
        }

        productService.save(product);

        List<Product> products = productService.getAllProducts();
        List<AdminProductsDto> productsDtos = getAdminProductsDtos(products);
        model.addAttribute("productsDtos", productsDtos);

        return "products-admin";
    }

    @DeleteMapping("/{id}")
    public String deleteProductAdmin(@PathVariable Long id, Model model) {
        if(productService.existsById(id)){
            productService.deleteById(id);
        }

        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.getAllProducts();
        List<AdminProductsDto> productsDtos = getAdminProductsDtos(products);
        model.addAttribute("productsDtos", productsDtos);
        addToModelBasicAttributes(model, categories);

        return "products-admin";
    }

    private void addToModelBasicAttributes(Model model, List<Category> categories) {
        model.addAttribute("categories", categories);
        model.addAttribute("filterList", FILTER_LIST);
        model.addAttribute("colorMap", COLOR_MAP);
        model.addAttribute("materialList", MATERIAL_LIST);
        model.addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }

    private List<Category> getListCategoriesForProduct(Product product){
        List<Category> categories = categoryService.findAll();
        if(product.getCategoryID() != null){
            categories.remove(product.getCategoryID());
            categories.add(0, product.getCategoryID());
        }
        return categories;
    }

    private Map<Material, Boolean> getMaterialMapForProduct(Material[] materials){
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
        return materialMap;
    }

    private Material[] getMaterialArrayFromStringArray(String[] productMaterials){
        Material[] materials = new Material[productMaterials.length];
        int i = 0;
        for(String s : productMaterials){
            for(Material m : MATERIAL_LIST){
                if(s.equals(m.name())) {
                    materials[i] = m;
                    i++;
                }
            }
        }
        return materials;
    }

    private List<AdminProductsDto> getAdminProductsDtos(List<Product> products) {
        List<AdminProductsDto> productsDtos = new ArrayList<>();
        for(Product product : products){
            productsDtos.add(new AdminProductsDto(product));
        }
        return productsDtos;
    }
}
