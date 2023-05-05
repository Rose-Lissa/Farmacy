package our.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import our.pharmacy.model.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
