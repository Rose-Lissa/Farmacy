package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.MedicineItem;
import our.pharmacy.model.PurchaseOrder;

import java.util.List;

@Repository
public interface MedicineItemRepository extends JpaRepository<MedicineItem, Long> {
    List<MedicineItem> findAllByPurchaseOrder(PurchaseOrder medicine);
}
