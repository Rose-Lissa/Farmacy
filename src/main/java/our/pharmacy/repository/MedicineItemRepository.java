package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.MedicineItem;

@Repository
public interface MedicineItemRepository extends JpaRepository<MedicineItem, Long> {
}
