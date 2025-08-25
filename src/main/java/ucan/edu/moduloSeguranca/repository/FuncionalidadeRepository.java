package ucan.edu.moduloSeguranca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;

import java.util.List;

public interface FuncionalidadeRepository extends JpaRepository<FuncionalidadeModel, Integer> {
    List<FuncionalidadeModel> findByAppPkApp(Integer appId);
    FuncionalidadeModel findByDesignacao(String designacao);
}
