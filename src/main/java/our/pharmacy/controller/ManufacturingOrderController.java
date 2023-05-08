package our.pharmacy.controller;

import org.springframework.web.bind.annotation.*;
import our.pharmacy.dto.ManufacturingOrderDto;
import our.pharmacy.service.ManufacturingOrderService;

@RestController
public class ManufacturingOrderController {

    private final ManufacturingOrderService manufacturingOrderService;


    public ManufacturingOrderController(ManufacturingOrderService manufacturingOrderService) {
        this.manufacturingOrderService = manufacturingOrderService;
    }

    @PostMapping("/manufacturing")
    private void createManufacturingOrder(@RequestBody ManufacturingOrderDto manufacturingOrderDto) {
        manufacturingOrderService.makeExternalOrderWithMissingMedicineInPurchaseOrder(manufacturingOrderDto);
    }

    @PatchMapping("/manufacturing")
    private void setReadyManufacturingOrder(@RequestParam Long id) {
        manufacturingOrderService.setManufacturingOrderReady(id);
    }
}
