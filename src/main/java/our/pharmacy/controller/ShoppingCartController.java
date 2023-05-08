package our.pharmacy.controller;

import org.springframework.web.bind.annotation.*;
import our.pharmacy.dto.ShoppingCartItemDto;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.model.ShoppingCartItem;
import our.pharmacy.service.PurchaseOrderService;

import java.util.List;

@RestController
public class ShoppingCartController {

    private final PurchaseOrderService purchaseOrderService;


    public ShoppingCartController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/api/cart")
    private void makeOrder() {
        purchaseOrderService.makeOrder();
    }

    @PatchMapping("/api/cart")
    private void updateShoppingCart(@RequestBody ShoppingCartItemDto dto) {
        purchaseOrderService.updateShoppingCart(dto);
    }

    @GetMapping("/api/cart")
    private PurchaseOrder getShoppingCart() {
        return purchaseOrderService.getShoppingCart();
    }

    @DeleteMapping("/api/cart")
    private void clearShoppingCart() {
        purchaseOrderService.clearShoppingCart();
    }
}
