package com.epam.furniturestoreapp.controller;

import com.epam.furniturestoreapp.entity.Category;
import com.epam.furniturestoreapp.entity.Product;
import com.epam.furniturestoreapp.model.AdminProductsDto;
import com.epam.furniturestoreapp.model.Color;
import com.epam.furniturestoreapp.model.Material;
import com.epam.furniturestoreapp.model.ProductDto;
import com.epam.furniturestoreapp.service.CategoryService;
import com.epam.furniturestoreapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.io.IOException;
import java.util.*;

import static com.epam.furniturestoreapp.model.StaticVariables.*;
import static com.epam.furniturestoreapp.model.StaticVariables.TH_ACTION_FOR_ALL_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdminProductsControllerTest {
    @InjectMocks
    private AdminProductsController adminProductsController;

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Test
    public void testGetProductsAdmin() {
        List<Product> mockProducts = new ArrayList<>();
        List<Category> mockCategories = new ArrayList<>();

        when(productService.getAllProducts()).thenReturn(mockProducts);
        when(categoryService.findAll()).thenReturn(mockCategories);

        String viewName = adminProductsController.getProductsAdmin(model);

        verify(productService, times(1)).getAllProducts();
        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute("productsDtos", mockProducts);
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin", viewName);
    }

    @Test
    public void testGetAddProductAdmin() {
        List<Category> mockCategories = new ArrayList<>();
        when(categoryService.findAll()).thenReturn(mockCategories);

        String viewName = adminProductsController.getAddProductAdmin(model);

        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute("materialMap", null);
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin-add-edit", viewName);
    }

    @Test
    public void testPostAddProductAdmin_Success() throws IOException {
        List<Category> mockCategories = new ArrayList<>();
        List<AdminProductsDto> productsDtos  = new ArrayList<>();
        ProductDto productDto = getTestProductDto();

        Category testCategory = getTestCategory();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(categoryService.findByName(any())).thenReturn(testCategory);

        String viewName = adminProductsController.postAddProductAdmin(productDto, bindingResult, model);

        verify(productService, times(1)).save(any(Product.class));
        verifyBasicModelAttributes(mockCategories);
        verify(model, times(1)).addAttribute("productsDtos", productsDtos);

        assertEquals("products-admin", viewName);
    }

    @Test
    public void testPostAddProductAdmin_Failure() throws IOException {
        List<Category> mockCategories = new ArrayList<>();
        ProductDto productDto = getTestProductDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminProductsController.postAddProductAdmin(productDto, bindingResult, model);

        verify(productService, never()).save(any(Product.class));
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin-add-edit", viewName);
    }

    @Test
    public void testGetEditProductAdmin() {
        long productId = 1L;
        Product product = getTestProduct();

        List<Category> mockCategories = new ArrayList<>();

        when(productService.getProductById(productId)).thenReturn(product);
        when(categoryService.findAll()).thenReturn(mockCategories);

        String viewName = adminProductsController.getEditProductAdmin(productId, model);

        verify(productService, times(1)).getProductById(productId);
        verify(categoryService, times(1)).findAll();
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin-add-edit", viewName);
    }

    @Test
    public void testPutEditProductAdmin_Success() throws IOException {
        List<Category> mockCategories = new ArrayList<>();
        long productId = 1L;
        Product product = getTestProduct();

        ProductDto productDto = getTestProductDto();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.getProductById(productId)).thenReturn(product);
        when(categoryService.findByName(any())).thenReturn(getTestCategory());

        String viewName = adminProductsController.putEditProductAdmin(productId, productDto, bindingResult, model);

        verify(productService, times(1)).save(any(Product.class));
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin", viewName);
    }

    @Test
    public void testPutEditProductAdmin_Failure() throws IOException {
        List<Category> mockCategories = new ArrayList<>();
        long productId = 1L;
        ProductDto productDto = getTestProductDto();
        Product product = getTestProduct();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(productService.getProductById(productId)).thenReturn(product);

        String viewName = adminProductsController.putEditProductAdmin(productId, productDto, bindingResult, model);

        verify(productService, never()).save(any(Product.class));
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin-add-edit", viewName);
    }

    @Test
    public void testDeleteProductAdmin() {
        Long productId = 1L;
        List<Category> mockCategories = new ArrayList<>();
        List<Product> mockProducts = new ArrayList<>();

        when(productService.existsById(productId)).thenReturn(true);
        doNothing().when(productService).deleteById(productId);
        when(categoryService.findAll()).thenReturn(mockCategories);
        when(productService.getAllProducts()).thenReturn(mockProducts);

        String viewName = adminProductsController.deleteProductAdmin(productId, model);

        verify(productService, times(1)).deleteById(productId);
        verify(categoryService, times(1)).findAll();
        verify(model, times(1)).addAttribute("productsDtos", mockProducts);
        verifyBasicModelAttributes(mockCategories);

        assertEquals("products-admin", viewName);
    }

    private void verifyBasicModelAttributes(List<Category> mockCategories){
        verify(model, times(1)).addAttribute("categories", mockCategories);
        verify(model, times(1)).addAttribute("filterList", FILTER_LIST);
        verify(model, times(1)).addAttribute("colorMap", COLOR_MAP);
        verify(model, times(1)).addAttribute("materialList", MATERIAL_LIST);
        verify(model, times(1)).addAttribute("thAction", TH_ACTION_FOR_ALL_PRODUCTS);
    }

    private Product getTestProduct(){
        Product product = new Product("productName", "productDescription",
                getTestCategory(), 100.0, 100, "dimensions", "material",
                "color", 5.0);
        product.setImage(new byte[1]);
        return product;
    }

    private ProductDto getTestProductDto(){
        ProductDto productDto = new ProductDto("productName", "productDescription",
                100.0, 100, "dimensions", new Material[]{Material.GLASS}, Color.BLUE);
        productDto.setImage(new MockMultipartFile("name", new byte[3]));
        return productDto;
    }

    private Category getTestCategory(){
        Category testCategory = new Category();
        testCategory.setCategoryID(1L);
        testCategory.setCategoryName("testCategory");
        return testCategory;
    }
}
