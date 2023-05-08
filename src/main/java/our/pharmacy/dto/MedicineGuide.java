package our.pharmacy.dto;

import our.pharmacy.model.Medicine;

import java.util.List;

public record MedicineGuide(
        List<Medicine> medicines,
        Integer quantityPages
) {
}
