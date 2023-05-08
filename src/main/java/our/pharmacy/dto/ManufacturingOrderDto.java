package our.pharmacy.dto;

public record ManufacturingOrderDto(Long purchaseOrderId, Long medicineId, Integer amount) {
}
