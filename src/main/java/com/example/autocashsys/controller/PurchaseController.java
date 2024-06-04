package com.example.autocashsys.controller;

import com.example.autocashsys.constants.AppConstants;
import com.example.autocashsys.converter.DefaultProductConverter;
import com.example.autocashsys.dto.SessionCart;
import com.example.autocashsys.dto.DefaultProductDto;
import com.example.autocashsys.entity.CartEntry;
import com.example.autocashsys.entity.Product;
import com.example.autocashsys.entity.Receipt;
import com.example.autocashsys.service.CartEntryService;
import com.example.autocashsys.service.EmployeeService;
import com.example.autocashsys.service.ProductService;
import com.example.autocashsys.service.ReceiptService;
import com.example.autocashsys.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@SessionAttributes("cart")
public class PurchaseController {

    private static final String EMPLOYEE_NOT_FOUND_FORMAT_ERROR_MSG = "Employee with username '%s' not found";
    private static final String PRODUCT_NOT_FOUND_FORMAT_ERROR_MSG = "Product with barcode '%s' not found";
    private static final String PRODUCT_WITH_BARCODE_LOG_MSG = "Product with barcode {} added to cart (amount: {})";
    private static final String CART_APPROVED_LOG_MSG = "Cart approved by {}";
    private static final String EMPLOYEE_NOT_FOUND_ERROR_LOG_MSG = "Employee with username {} not found";

    private final ProductService productService;
    private final EmployeeService employeeService;
    private final ReceiptService receiptService;
    private final CartEntryService cartEntryService;
    private final DefaultProductConverter productConverter;

    @ModelAttribute("cart")
    public SessionCart createCart() {
        return new SessionCart();
    }

    @GetMapping("/form_purchase")
    public String purchase(Model model,
                           @RequestParam(value = "search_param", required = false) String searchParam,
                           @RequestParam(value = "search_pattern", required = false) String searchPattern) {

        List<DefaultProductDto> products = new ArrayList<>();
        if (StringUtils.isNotEmpty(searchPattern)) {
            products = productService.getProductsByCriteria(searchParam, searchPattern).stream()
                    .map(productConverter::convert)
                    .toList();
        }

        model.addAttribute(AppConstants.PRODUCTS_ATTR, products);
        model.addAttribute(AppConstants.SEARCH_PARAM_ATTR, searchParam);
        model.addAttribute(AppConstants.SEARCH_PATTERN_ATTR, searchPattern);
        return "form_purchase";
    }

    @PostMapping("/put_to_cart")
    public String addToCart(@ModelAttribute("cart") SessionCart cart,
                            @RequestParam("barcode") String barcode, @RequestParam("amount") Integer amount) {
        productService.getForBarcode(barcode).ifPresent(product -> {
            cart.put(productConverter.convert(product), amount);
            log.info(PRODUCT_WITH_BARCODE_LOG_MSG, barcode, amount);
        });
        return "redirect:/form_purchase";
    }

    @PostMapping("/edit_cart")
    public String editCart(@ModelAttribute("cart") SessionCart cart,
                           @RequestParam("barcode") String barcode, @RequestParam("amount") Integer amount) {
        productService.getForBarcode(barcode).ifPresent(product -> cart.replace(productConverter.convert(product), amount));
        return "redirect:/form_purchase";
    }

    @PostMapping("/delete_from_cart")
    public String deleteFromCart(@ModelAttribute("cart") SessionCart cart, @RequestParam("barcode") String barcode) {
        productService.getForBarcode(barcode).ifPresent(product -> cart.delete(productConverter.convert(product)));
        return "redirect:/form_purchase";
    }

    @PostMapping("/cancel_purchase")
    public String cancelPurchase(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping("/pay_for_purchase")
    public String payForPurchase(Model model, @ModelAttribute("cart") SessionCart cart) {
        if (cart.getIsRequiredVerification()) {
            model.addAttribute(AppConstants.TOGGLE_VERIFICATION_DIALOG_ATTR, true);
            return "form_purchase";
        }
        return "pay_for_purchase";
    }

    @PostMapping("/approve_cart")
    @ResponseBody
    public ResponseEntity<?> payForPurchaseAfterApprove(@ModelAttribute("cart") SessionCart cart,
                                                        @RequestParam("employeeUsername") String employeeUsername) {
        var optionalEmployee = employeeService.getForUsername(employeeUsername);
        if (optionalEmployee.isPresent()) {
            cart.setIsRequiredVerification(false);
            cart.setApprover(optionalEmployee.get());
            log.info(CART_APPROVED_LOG_MSG, employeeUsername);
            return ResponseEntity.ok().build();
        }
        log.error(EMPLOYEE_NOT_FOUND_ERROR_LOG_MSG, employeeUsername);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(formErrorMessage(EMPLOYEE_NOT_FOUND_FORMAT_ERROR_MSG, employeeUsername));
    }

    @Transactional
    @PostMapping("/pay_for_purchase")
    @ResponseBody
    public ResponseEntity<?> payForPurchaseProcession(@ModelAttribute("cart") SessionCart cart, SessionStatus sessionStatus) {
        Receipt receipt = prepareReceipt(cart);
        receiptService.createReceipt(receipt);
        receipt.getCartEntries().forEach(cartEntryService::createCartEntry);
        sessionStatus.setComplete();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private Receipt prepareReceipt(SessionCart cart) {
        Receipt receipt = new Receipt();
        receipt.setCreatedAt(LocalDateTime.now());
        receipt.setApprover(cart.getApprover());
        receipt.setCartEntries(prepareCartEntries(cart, receipt));
        return receipt;
    }

    private List<CartEntry> prepareCartEntries(SessionCart cart, Receipt receipt) {
        return cart.getProducts().entrySet().stream()
                .map(entry -> prepareCartEntry(receipt,
                        productService.getForBarcode(entry.getKey().getBarcode()).orElseThrow(
                                () -> new RuntimeException(formErrorMessage(PRODUCT_NOT_FOUND_FORMAT_ERROR_MSG, entry.getKey().getBarcode()))),
                        entry.getValue().getAmount()))
                .toList();
    }

    private CartEntry prepareCartEntry(Receipt receipt, Product product, Integer amount) {
        CartEntry result = new CartEntry();
        result.setReceipt(receipt);
        result.setProduct(product);
        result.setAmount(amount);
        result.setUnitPrice(product.getPrice());
        return result;
    }

    private String formErrorMessage(String message, Object... args) {
        return String.format(message, args);
    }
}
