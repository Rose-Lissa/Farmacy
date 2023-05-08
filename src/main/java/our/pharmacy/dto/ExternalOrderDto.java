package our.pharmacy.dto;

public record ExternalOrderDto(Long purchaseOrderId, Long supplierId, Long medicineId, Integer amount) {
}
