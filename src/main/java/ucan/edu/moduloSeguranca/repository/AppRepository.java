package ucan.edu.moduloSeguranca.repository;

import ucan.edu.moduloSeguranca.models.AppModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<AppModel, Integer>
{

    boolean existsByNome(String nome);       // ✅ verifica duplicados
    Optional<AppModel> findByNome(String nome); // ✅ buscar App por nome
}
