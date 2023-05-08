package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.PurchaseOrder;
import our.pharmacy.model.PurchaseOrderStage;
import our.pharmacy.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findPurchaseOrderByUserAndStage(User user, PurchaseOrderStage stage);

    List<PurchaseOrder> getPurchaseOrdersByStageNotIn(List<PurchaseOrderStage> manufacturingStages);
}
