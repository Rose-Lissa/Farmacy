package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.MedicineType;

@Repository
public interface MedicineTypeRepository extends JpaRepository<MedicineType, Long> {

}
