package our.pharmacy.dto;

import our.pharmacy.model.MedicineType;

public record MedicineFilter(
        Integer pageNumber,
        Integer pageSize,
        String name,
        Long startPrice,
        Long endPrice,
        Long medicineTypeId,
        SortOrder sortOrder,
        SortField sortField

) {
    public enum SortOrder{
        ASC, DSC
    }
    public enum SortField{
        ID("id"), NAME("name"), PRICE("price");

        private String fieldName;

        SortField(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }
}
