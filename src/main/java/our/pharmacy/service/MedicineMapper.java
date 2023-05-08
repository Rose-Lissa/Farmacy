package our.pharmacy.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import our.pharmacy.dto.MedicineDto;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.MedicineType;

@Mapper
public interface MedicineMapper {
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "derived", ignore = true)
    @Mapping(target = "medicineType", source = "medicineType")
    @Mapping(target = "id", source = "medicineDto.id")
    @Mapping(target = "name", source = "medicineDto.name")
    Medicine map(MedicineDto medicineDto, MedicineType medicineType);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "derived", ignore = true)
    @Mapping(target = "medicineType", source = "medicineType")
    @Mapping(target = "id", source = "medicineDto.id")
    @Mapping(target = "name", source = "medicineDto.name")
    void map(@MappingTarget Medicine medicine, MedicineDto medicineDto, MedicineType medicineType);

    @Mapping(target = "medicineTypeId", source = "medicineType.id")
    MedicineDto map(Medicine medicineById);
}
