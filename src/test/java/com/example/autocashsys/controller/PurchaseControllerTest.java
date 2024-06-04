package com.example.autocashsys.controller;

import com.example.autocashsys.constants.AppConstants;
import com.example.autocashsys.converter.DefaultProductConverter;
import com.example.autocashsys.dto.DefaultProductDto;
import com.example.autocashsys.dto.SessionCart;
import com.example.autocashsys.entity.CartEntry;
import com.example.autocashsys.entity.Employee;
import com.example.autocashsys.entity.Product;
import com.example.autocashsys.entity.Receipt;
import com.example.autocashsys.service.CartEntryService;
import com.example.autocashsys.service.EmployeeService;
import com.example.autocashsys.service.ProductService;
import com.example.autocashsys.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {

    private static final String EMPLOYEE_NOT_FOUND_EXPECTED_ERROR_MSG = "Employee with username 'username' not found";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private ReceiptService receiptService;
    @MockBean
    private CartEntryService cartEntryService;
    @MockBean
    private DefaultProductConverter productConverter;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testFormPurchase() throws Exception {
        String searchParam = "searchParam";
        String searchPattern = "searchPattern";
        Product product = new Product();
        DefaultProductDto productDto = new DefaultProductDto();

        when(productService.getProductsByCriteria(searchParam, searchPattern))
                .thenReturn(Collections.singletonList(product));
        when(productConverter.convert(product)).thenReturn(productDto);

        mockMvc.perform(get("/form_purchase")
                        .param("search_param", searchParam)
                        .param("search_pattern", searchPattern))
                .andExpect(status().isOk())
                .andExpect(view().name("form_purchase"))
                .andExpect(model().attribute(AppConstants.PRODUCTS_ATTR,
                        Collections.singletonList(productDto)))
                .andExpect(model().attribute(AppConstants.SEARCH_PARAM_ATTR, searchParam))
                .andExpect(model().attribute(AppConstants.SEARCH_PATTERN_ATTR, searchPattern));

        verify(productService).getProductsByCriteria(searchParam, searchPattern);
        verify(productConverter).convert(product);
    }

    @Test
    public void testAddToCart() throws Exception {
        String barcode = "barcode";
        int amount = 5;
        Product product = new Product();
        DefaultProductDto productDto = new DefaultProductDto();
        productDto.setIsRequiredVerification(Boolean.FALSE);
        productDto.setPrice(new BigDecimal("10"));
        SessionCart cart = new SessionCart();

        when(productService.getForBarcode(barcode)).thenReturn(Optional.of(product));
        when(productConverter.convert(product)).thenReturn(productDto);

        mockMvc.perform(post("/put_to_cart")
                        .sessionAttr("cart", cart)
                        .param("barcode", barcode)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/form_purchase"));

        assertEquals(amount, cart.getProducts().get(productDto).getAmount());

        verify(productService).getForBarcode(barcode);
        verify(productConverter).convert(product);
    }

    @Test
    public void testEditCart() throws Exception {
        String barcode = "barcode";
        int amount = 5;

        Product product = new Product();
        DefaultProductDto productDto = new DefaultProductDto();
        productDto.setIsRequiredVerification(Boolean.FALSE);
        productDto.setPrice(new BigDecimal("10"));

        SessionCart cart = new SessionCart();
        cart.put(productDto, 1);

        when(productService.getForBarcode(barcode)).thenReturn(Optional.of(product));
        when(productConverter.convert(product)).thenReturn(productDto);

        mockMvc.perform(post("/edit_cart")
                        .sessionAttr("cart", cart)
                        .param("barcode", barcode)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/form_purchase"));

        assertEquals(amount, cart.getProducts().get(productDto).getAmount());

        verify(productService).getForBarcode(barcode);
        verify(productConverter).convert(product);
    }

    @Test
    public void testDeleteFromCart() throws Exception {
        String barcode = "barcode";
        Product product = new Product();
        DefaultProductDto productDto = new DefaultProductDto();
        productDto.setIsRequiredVerification(Boolean.FALSE);
        productDto.setPrice(new BigDecimal("10"));

        SessionCart cart = new SessionCart();
        cart.put(productDto, 1);

        when(productService.getForBarcode(barcode)).thenReturn(Optional.of(product));
        when(productConverter.convert(product)).thenReturn(productDto);

        mockMvc.perform(post("/delete_from_cart")
                        .sessionAttr("cart", cart)
                        .param("barcode", barcode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/form_purchase"));

        assertFalse(cart.getProducts().containsKey(productDto));

        verify(productService).getForBarcode(barcode);
        verify(productConverter).convert(product);
    }

    @Test
    public void testCancelPurchase() throws Exception {
        mockMvc.perform(post("/cancel_purchase"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testPayForPurchase() throws Exception {
        SessionCart cart = new SessionCart();
        cart.setIsRequiredVerification(false);

        mockMvc.perform(get("/pay_for_purchase").sessionAttr("cart", cart))
                .andExpect(status().isOk())
                .andExpect(view().name("pay_for_purchase"));

        cart.setIsRequiredVerification(true);

        mockMvc.perform(get("/pay_for_purchase").sessionAttr("cart", cart))
                .andExpect(status().isOk())
                .andExpect(view().name("form_purchase"))
                .andExpect(model().attributeExists(AppConstants.TOGGLE_VERIFICATION_DIALOG_ATTR));
    }

    @Test
    public void testPayForPurchaseAfterApprove() throws Exception {
        String username = "username";
        SessionCart cart = new SessionCart();

        Employee employee = new Employee();
        employee.setUsername(username);

        when(employeeService.getForUsername(username)).thenReturn(Optional.of(employee));

        mockMvc.perform(post("/approve_cart")
                        .sessionAttr("cart", cart)
                        .param("employeeUsername", username))
                .andExpect(status().isOk());

        assertFalse(cart.getIsRequiredVerification());
        assertEquals(employee, cart.getApprover());

        verify(employeeService).getForUsername(username);
    }

    @Test
    public void testPayForPurchaseAfterApproveEmployeeNotFound() throws Exception {
        String username = "username";
        SessionCart cart = new SessionCart();

        when(employeeService.getForUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(post("/approve_cart")
                        .sessionAttr("cart", cart)
                        .param("employeeUsername", username))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(containsString(EMPLOYEE_NOT_FOUND_EXPECTED_ERROR_MSG)));

        verify(employeeService).getForUsername(username);
    }

    @Test
    public void testPayForPurchaseProcession() throws Exception {
        int amount = 5;
        SessionCart cart = new SessionCart();
        Employee approver = new Employee();
        cart.setApprover(approver);
        for (int i = 0; i < amount; i++) {
            String barcode = "barcode"+i;
            Product product = new Product();
            product.setPrice(BigDecimal.ONE);
            DefaultProductDto productDto = new DefaultProductDto();
            productDto.setIsRequiredVerification(Boolean.FALSE);
            productDto.setBarcode(barcode);
            productDto.setPrice(BigDecimal.ONE);

            cart.put(productDto, 1);
            when(productService.getForBarcode(barcode)).thenReturn(Optional.of(product));
        }

        mockMvc.perform(post("/pay_for_purchase")
                        .sessionAttr("cart", cart))
                .andExpect(status().isCreated());

        verify(receiptService, times(1)).createReceipt(any(Receipt.class));
        verify(cartEntryService, times(amount)).createCartEntry(any(CartEntry.class));
    }
}