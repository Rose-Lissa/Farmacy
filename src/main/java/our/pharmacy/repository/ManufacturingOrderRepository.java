package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.ManufacturingOrder;

@Repository
public interface ManufacturingOrderRepository extends JpaRepository<ManufacturingOrder, Long> {
}
