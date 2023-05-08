package our.pharmacy.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.pharmacy.dto.ShoppingCartItemDto;
import our.pharmacy.event.ExternalOrderCreatedEvent;
import our.pharmacy.event.ExternalOrderReadyEvent;
import our.pharmacy.event.ManufacturingOrderCreatedEvent;
import our.pharmacy.event.ManufacturingOrderReadyEvent;
import our.pharmacy.exception.BusinessException;
import our.pharmacy.model.*;
import our.pharmacy.repository.PurchaseOrderRepository;
import our.pharmacy.repository.ShoppingCartRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final UserService userService;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private final PurchaseOrderMapper purchaseOrderMapper;

    private final MedicineService medicineService;

    private final MedicineItemService medicineItemService;

    @Autowired
    public PurchaseOrderService(UserService userService,
                                PurchaseOrderRepository purchaseOrderRepository,
                                ShoppingCartRepository shoppingCartRepository,
                                PurchaseOrderMapper purchaseOrderMapper,
                                MedicineService medicineService,
                                MedicineItemService medicineItemService) {
        this.userService = userService;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.medicineService = medicineService;
        this.medicineItemService = medicineItemService;
    }

    @Transactional
    public void updateShoppingCart(ShoppingCartItemDto shoppingCartItemDto) {
        try {
            User orderOwner = userService.getCurrentUser();
            PurchaseOrder order = getOrCreatePurchaseOrderIfOrderInStageCreatingNotFound(orderOwner);
            Medicine medicine = medicineService.getMedicineReferenceById(shoppingCartItemDto.medicineId());
            Integer medicineAmount = shoppingCartItemDto.amount();

            if (shoppingCartItemDto.amount() < 0) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Try to add product to shopping cart with amount < 0");
            } else if (shoppingCartItemDto.amount() == 0) {
                shoppingCartRepository.deleteAllByMedicineAndPurchaseOrder(medicine, order);
            } else {
                shoppingCartRepository.findShoppingCartItemByMedicineAndPurchaseOrder(medicine, order)
                        .ifPresentOrElse(
                                (item) -> {
                                    item.setNumber(medicineAmount);
                                    shoppingCartRepository.save(item);
                                },
                                () -> shoppingCartRepository.save(purchaseOrderMapper.map(order, medicine, medicineAmount))
                        );
            }
        } catch (EntityNotFoundException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Try to add not exists product to shopping cart");
        }
    }

    public void clearShoppingCart() {
        User orderOwner = userService.getCurrentUser();
        PurchaseOrder order = getPurchaseOrderInStageCreating(orderOwner);
        order.setShoppingCartItems(Collections.emptyList());
    }

    @Transactional
    public void makeOrder() {
        User orderOwner = userService.getCurrentUser();
        PurchaseOrder order = getPurchaseOrderInStageCreating(orderOwner);
        if (order.getShoppingCartItems().isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Try to make EMPTY order");
        }

        reservation(order);

        if (isReady(order)) {
            order.setStage(PurchaseOrderStage.READY);
        } else {
            order.setStage(PurchaseOrderStage.PROCESSING);
        }
    }

    public List<PurchaseOrder> getActiveOrders() {
        User user = userService.getCurrentUser();
        return switch (user.getRole()) {

            case ROLE_CONSUMER ->
                purchaseOrderRepository.getPurchaseOrdersByStageNotIn(
                                List.of(
                                        PurchaseOrderStage.CLOSED,
                                        PurchaseOrderStage.CANCELLED,
                                        PurchaseOrderStage.CREATING
                                )).stream()
                        .filter(purchaseOrder -> purchaseOrder.getUser().getId().equals(user.getId()))
                        .collect(Collectors.toList());
            case ROLE_WORKER -> purchaseOrderRepository.getPurchaseOrdersByStageNotIn(List.of(PurchaseOrderStage.CLOSED, PurchaseOrderStage.CANCELLED, PurchaseOrderStage.CREATING));
            case ROLE_ADMIN, SIMPLE_USER ->
                Collections.emptyList();

        };
    }

    private void reservation(PurchaseOrder order) {
        order.getShoppingCartItems().forEach((item) -> {
            Medicine medicine = item.getMedicine();
            reservation(order, item, medicine);
        });
    }

    private void reservation(PurchaseOrder order, ShoppingCartItem item, Medicine medicine) {
        medicineItemService.saveAll(medicine.getItems().stream()
                .filter(MedicineItem::isLiberty)
                .limit(item.getNumber())
                .peek((e) -> e.setPurchaseOrder(order))
                .collect(Collectors.toList()));
    }

    private boolean isReady(PurchaseOrder order) {
        return order.getShoppingCartItems().stream().noneMatch((shoppingCartItem ->
                order.getMedicineItems().stream()
                        .filter(medicineItem -> medicineItem.getMedicine() == shoppingCartItem.getMedicine())
                        .count() != shoppingCartItem.getNumber()
        ));
    }

    public void cancelOrder(Long orderId) {
        PurchaseOrder order = getPurchaseOrderById(orderId);
        if (order.getStage() == PurchaseOrderStage.CLOSED || order.getStage() == PurchaseOrderStage.CREATING) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Try to cancel order in stage: " + order.getStage());
        }

        order.setStage(PurchaseOrderStage.CANCELLED);
    }

    public void completeOrder(Long orderId) {
        if (getPurchaseOrderById(orderId).getStage() != PurchaseOrderStage.READY) {
            throw new BusinessException(HttpStatus.BAD_REQUEST);
        }

        PurchaseOrder order = getPurchaseOrderById(orderId);
        order.setMedicineItems(order.getMedicineItems().stream().peek((medicineItem -> medicineItem.setSales(true))).collect(Collectors.toList()));
        order.setStage(PurchaseOrderStage.CLOSED);
        purchaseOrderRepository.save(order);
    }


    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id).orElseThrow(() ->
                new BusinessException(HttpStatus.BAD_REQUEST, "Order not exists"));
    }


    @EventListener(value = ManufacturingOrderReadyEvent.class)
    public void onManufacturingOrderReadyEvent(ManufacturingOrderReadyEvent event) {
        ManufacturingOrder manufacturingOrder = event.getManufacturingOrder();
        PurchaseOrder order = manufacturingOrder.getPurchaseOrder();

        medicineItemService.addMedicineItemToPurchase(manufacturingOrder.getMedicine(), order, manufacturingOrder.getNumber());

        if (isReady(order)) {
            order.setStage(PurchaseOrderStage.READY);
        }
    }

    @EventListener(value = ManufacturingOrderCreatedEvent.class)
    public void onManufacturingOrderCreatedEvent(ManufacturingOrderCreatedEvent event) {
        ManufacturingOrder manufacturingOrder = event.getManufacturingOrder();
        PurchaseOrder order = manufacturingOrder.getPurchaseOrder();

        if (isManufacturing(order)) {
            order.setStage(PurchaseOrderStage.MANUFACTURING);
        }
    }

    @EventListener(value = ExternalOrderReadyEvent.class)
    public void onExternalOrderReadyEvent(ExternalOrderReadyEvent event) {
        ExternalOrder externalOrder = event.getExternalOrder();
        PurchaseOrder order = externalOrder.getPurchaseOrder();

        medicineItemService.addMedicineItemToPurchase(externalOrder.getMedicine(), order, externalOrder.getNumber());

        if (isReady(order)) {
            order.setStage(PurchaseOrderStage.READY);
        }
    }

    @EventListener(value = ExternalOrderCreatedEvent.class)
    public void onExternalOrderCreatedEvent(ExternalOrderCreatedEvent event) {
        ExternalOrder externalOrder = event.getExternalOrder();
        PurchaseOrder order = externalOrder.getPurchaseOrder();

        if (isManufacturing(order)) {
            order.setStage(PurchaseOrderStage.MANUFACTURING);
        }
    }

    private boolean isManufacturing(PurchaseOrder order) {
        return order.getShoppingCartItems().stream().noneMatch((shoppingCartItem -> {
            long reservedQuantity = order.getMedicineItems().stream()
                    .filter(medicineItem -> medicineItem.getMedicine() == shoppingCartItem.getMedicine())
                    .count();
            long inManufacturingQuantity = Long.valueOf(shoppingCartItem.getDerived().getQuantityInManufacturing());
            long inExternalQuantity = Long.valueOf(shoppingCartItem.getDerived().getQuantityInExternal());
            return reservedQuantity + inManufacturingQuantity + inExternalQuantity >= shoppingCartItem.getNumber();
        }));
    }

    private PurchaseOrder getPurchaseOrderInStageCreating(User orderOwner) {
        return purchaseOrderRepository.findPurchaseOrderByUserAndStage(orderOwner, PurchaseOrderStage.CREATING)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Order not exists"));
    }

    private PurchaseOrder getOrCreatePurchaseOrderIfOrderInStageCreatingNotFound(User orderOwner) {
        return purchaseOrderRepository.findPurchaseOrderByUserAndStage(orderOwner, PurchaseOrderStage.CREATING)
                .orElseGet(() -> createPurchaseOrder(orderOwner));
    }

    private PurchaseOrder createPurchaseOrder(User orderOwner) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setStage(PurchaseOrderStage.CREATING);
        purchaseOrder.setUser(orderOwner);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder getShoppingCart() {
        User orderOwner = userService.getCurrentUser();
        return getOrCreatePurchaseOrderIfOrderInStageCreatingNotFound(orderOwner);
    }
}
