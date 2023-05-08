package our.pharmacy.controller;

import org.springframework.web.bind.annotation.*;
import our.pharmacy.dto.ExternalOrderDto;
import our.pharmacy.dto.ManufacturingOrderDto;
import our.pharmacy.model.ExternalOrder;
import our.pharmacy.service.ExternalOrderService;
import our.pharmacy.service.ManufacturingOrderService;

@RestController
public class ExternalOrderController {

    private final ExternalOrderService externalOrderService;


    public ExternalOrderController(ExternalOrderService externalOrderService) {
        this.externalOrderService = externalOrderService;
    }

    @PostMapping("/external")
    private void createExternalOrder(@RequestBody ExternalOrderDto externalOrderDto) {
        externalOrderService.makeExternalOrderWithMissingMedicineInPurchaseOrder(externalOrderDto);
    }

    @PatchMapping("/external")
    private void setReadyExternalOrder(@RequestParam Long id) {
        externalOrderService.setExternalOrderReady(id);
    }
}
