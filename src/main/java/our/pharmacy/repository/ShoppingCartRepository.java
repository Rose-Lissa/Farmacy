package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.Medicine;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.model.ShoppingCartItem;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartItem, Long> {

    Optional<ShoppingCartItem> findShoppingCartItemByMedicineAndPurchaseOrder(Medicine medicine, PurchaseOrder order);
    void deleteAllByMedicineAndPurchaseOrder(Medicine medicine, PurchaseOrder order);
}
