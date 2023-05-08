package our.pharmacy.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import our.pharmacy.dto.MedicineDto;
import our.pharmacy.dto.MedicineFilter;
import our.pharmacy.dto.MedicineGuide;
import our.pharmacy.exception.BusinessException;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.MedicineType;
import our.pharmacy.repository.MedicineRepository;
import our.pharmacy.repository.MedicineTypeRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;

    private final MedicineTypeRepository medicineTypeRepository;
    private final MedicineMapper medicineMapper;

    public MedicineService(MedicineRepository medicineRepository, MedicineTypeRepository medicineTypeRepository, MedicineMapper medicineMapper) {
        this.medicineRepository = medicineRepository;
        this.medicineTypeRepository = medicineTypeRepository;
        this.medicineMapper = medicineMapper;
    }



    public MedicineGuide getMedicineGuide(MedicineFilter medicineFilter) {
        Page<Medicine> page = medicineRepository.findAll(getSpec(medicineFilter,
                medicineTypeRepository.findById(medicineFilter.medicineTypeId() == null? 0: medicineFilter.medicineTypeId()).orElse(null)
        ), Pageable.ofSize(medicineFilter.pageSize()).withPage(medicineFilter.pageNumber()));

        return new MedicineGuide(page.stream().toList(), page.getTotalPages());
    }

    private Specification<Medicine> getSpec(MedicineFilter filter, MedicineType medicineType) {
        return ((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();

           if(filter.name() != null) {
               predicates.add(criteriaBuilder.like(root.get("name"), filter.name()+"%"));
           }

            if(filter.startPrice() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("price"), BigDecimal.valueOf(filter.startPrice())));
            }

            if(filter.endPrice() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("price"), BigDecimal.valueOf(filter.endPrice())));
            }

            if(medicineType != null) {
                predicates.add(criteriaBuilder.equal(root.get("medicineType"), medicineType));
            }

            if (filter.sortField() != null && filter.sortOrder() != null) {
                switch (filter.sortOrder()) {
                    case ASC -> {
                        query.orderBy(criteriaBuilder.asc(root.get(filter.sortField().getFieldName())));
                    }
                    case DSC -> {
                        query.orderBy(criteriaBuilder.desc(root.get(filter.sortField().getFieldName())));
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
    }

    public Medicine getMedicineReferenceById(Long id) {
        return medicineRepository.getReferenceById(id);
    }

    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Medicine not exists"));
    }

    public void createMedicine(MedicineDto medicineDto) {
        Medicine medicine = medicineMapper.map(medicineDto, medicineTypeRepository.getReferenceById(medicineDto.medicineTypeId()));
        medicineRepository.save(medicine);
    }

    public void updateMedicine(MedicineDto medicineDto) {
        Medicine medicine = getMedicineById(medicineDto.id());
        medicineMapper.map(medicine, medicineDto, medicineTypeRepository.getReferenceById(medicineDto.medicineTypeId()));
        medicineRepository.save(medicine);
    }

    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    public MedicineDto getMedicineDto(Long id) {
        return medicineMapper.map(getMedicineById(id));
    }
}
