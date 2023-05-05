package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.ShoppingCartMedicine;

@Repository
public interface ShoppingCartMedicineRepository extends JpaRepository<ShoppingCartMedicine, Long> {
}
