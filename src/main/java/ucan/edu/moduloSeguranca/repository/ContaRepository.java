package ucan.edu.moduloSeguranca.repository;

import ucan.edu.moduloSeguranca.models.ContaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<ContaModel, Integer> {
    Optional<ContaModel> findByUsername(String username);
    boolean existsByUsername(String username);
}
