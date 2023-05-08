package our.pharmacy.dto;

public record MedicineDto(
        Long id,
        String name,
        String price,
        String description,
        String manufacturingTechnology,
        Long medicineTypeId,
        Boolean isManufacturing
) {
}
