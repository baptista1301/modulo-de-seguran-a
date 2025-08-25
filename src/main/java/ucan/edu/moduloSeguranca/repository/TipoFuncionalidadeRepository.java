package ucan.edu.moduloSeguranca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucan.edu.moduloSeguranca.models.TipoFuncionalidadeModel;

import java.util.Optional;

@Repository
public interface TipoFuncionalidadeRepository extends JpaRepository<TipoFuncionalidadeModel,Integer>
{
    Optional<TipoFuncionalidadeModel> findByDesignacao(String designacao);

}
