package ucan.edu.moduloSeguranca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.edu.moduloSeguranca.models.GrupoModel;

public interface GrupoRepository extends JpaRepository<GrupoModel, Integer> {
    boolean existsByDesignacao(String designacao);
    GrupoModel findByDesignacao(String designacao);
}
