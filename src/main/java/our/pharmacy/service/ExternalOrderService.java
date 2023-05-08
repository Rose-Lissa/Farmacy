package our.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import our.pharmacy.dto.ExternalOrderDto;
import our.pharmacy.event.ExternalOrderCreatedEvent;
import our.pharmacy.event.ExternalOrderReadyEvent;
import our.pharmacy.exception.BusinessException;
import our.pharmacy.model.ExternalOrder;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.model.Supplier;
import our.pharmacy.repository.ExternalOrderRepository;
import our.pharmacy.repository.SupplierRepository;

@Service
public class ExternalOrderService {
    private final PurchaseOrderService purchaseOrderService;
    private final ExternalOrderRepository externalOrderRepository;
    private final MedicineService medicineService;
    private final SupplierRepository supplierRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ExternalOrderService(PurchaseOrderService purchaseOrderService, ExternalOrderRepository externalOrderRepository, MedicineService medicineService, SupplierRepository supplierRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.purchaseOrderService = purchaseOrderService;
        this.externalOrderRepository = externalOrderRepository;
        this.medicineService = medicineService;
        this.supplierRepository = supplierRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void makeExternalOrderWithMissingMedicineInPurchaseOrder(ExternalOrderDto externalOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderById(externalOrderDto.purchaseOrderId());
        Supplier supplier = getSupplierById(externalOrderDto.supplierId());
        Integer amount = externalOrderDto.amount();
        Medicine medicine = medicineService.getMedicineById(externalOrderDto.medicineId());

        ExternalOrder externalOrder = new ExternalOrder();
        externalOrder.setPurchaseOrder(purchaseOrder);
        externalOrder.setMedicine(medicine);
        externalOrder.setSupplier(supplier);
        externalOrder.setNumber(amount);

        externalOrder = externalOrderRepository.save(externalOrder);
        applicationEventPublisher.publishEvent(new ExternalOrderCreatedEvent(externalOrder));
        System.out.println("Заказ номер " + externalOrder.getId() +  " отправлен поставщику " + externalOrder.getSupplier().getName());
    }

    public void setExternalOrderReady(Long externalOrderId) {
        externalOrderRepository.findById(externalOrderId).stream()
                .peek(externalOrder -> externalOrder.setReady(true))
                .forEach(externalOrder -> {
                    applicationEventPublisher.publishEvent(
                            new ExternalOrderReadyEvent(
                                    externalOrderRepository.save(externalOrder)
                            )
                    );
                });
    }

    private Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(()-> new BusinessException(HttpStatus.BAD_REQUEST, "Supplier not found"));
    }
}
