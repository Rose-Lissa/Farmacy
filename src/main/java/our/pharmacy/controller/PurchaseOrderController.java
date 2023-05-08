package our.pharmacy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.service.PurchaseOrderService;

import java.util.List;

@RestController
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/order/active")
    public List<PurchaseOrder> getActiveOrders() {
        return purchaseOrderService.getActiveOrders();
    }

    @PatchMapping("/order")
    public void completeOrder(Long id) {
        purchaseOrderService.completeOrder(id);
    }
}
