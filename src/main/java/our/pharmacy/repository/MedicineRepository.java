package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> , JpaSpecificationExecutor<Medicine> {
}
