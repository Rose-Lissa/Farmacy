package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.Consumer;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
