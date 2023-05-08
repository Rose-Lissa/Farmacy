package our.pharmacy.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import our.pharmacy.dto.ExternalOrderDto;
import our.pharmacy.dto.ManufacturingOrderDto;
import our.pharmacy.event.ManufacturingOrderCreatedEvent;
import our.pharmacy.event.ManufacturingOrderReadyEvent;
import our.pharmacy.model.*;
import our.pharmacy.repository.ManufacturingOrderRepository;

@Service
public class ManufacturingOrderService {
    private final PurchaseOrderService purchaseOrderService;
    private final ManufacturingOrderRepository manufacturingOrderRepository;
    private final MedicineService medicineService;
    private final ApplicationEventPublisher applicationEventPublisher;


    public ManufacturingOrderService(PurchaseOrderService purchaseOrderService, ManufacturingOrderRepository manufacturingOrderRepository, MedicineService medicineService, ApplicationEventPublisher applicationEventPublisher) {
        this.purchaseOrderService = purchaseOrderService;
        this.manufacturingOrderRepository = manufacturingOrderRepository;
        this.medicineService = medicineService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void makeExternalOrderWithMissingMedicineInPurchaseOrder(ManufacturingOrderDto manufacturingOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(manufacturingOrderDto.purchaseOrderId());
        Integer amount = manufacturingOrderDto.amount();
        Medicine medicine = medicineService.getMedicineById(manufacturingOrderDto.medicineId());

        ManufacturingOrder manufacturingOrder = new ManufacturingOrder();
        manufacturingOrder.setPurchaseOrder(purchaseOrder);
        manufacturingOrder.setMedicine(medicine);
        manufacturingOrder.setNumber(amount);

        manufacturingOrder = manufacturingOrderRepository.save(manufacturingOrder);

        applicationEventPublisher.publishEvent(new ManufacturingOrderCreatedEvent(manufacturingOrder));
        System.out.println("Заказ номер " + manufacturingOrder.getId() + " на производство создан");
    }

    public void setManufacturingOrderReady(Long externalOrderId) {
        manufacturingOrderRepository.findById(externalOrderId).stream()
                .peek(manufacturingOrder -> manufacturingOrder.setReady(true))
                .forEach(manufacturingOrder -> applicationEventPublisher.publishEvent(new ManufacturingOrderReadyEvent(
                        manufacturingOrderRepository.save(manufacturingOrder)
                )));
    }
}
