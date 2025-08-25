package ucan.edu.moduloSeguranca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.moduloSeguranca.models.AppModel;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.repository.AppRepository;
import ucan.edu.moduloSeguranca.repository.FuncionalidadeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionalidadeService {

    @Autowired
    private FuncionalidadeRepository funcionalidadeRepository;

    @Autowired
    private AppRepository appRepository;

    public FuncionalidadeModel criarFuncionalidade(FuncionalidadeModel funcionalidade, Integer appId) {
        AppModel app = appRepository.findById(appId).orElseThrow();
        funcionalidade.setApp(app);
        return funcionalidadeRepository.save(funcionalidade);
    }

    public List<FuncionalidadeModel> listarTodas() {
        return funcionalidadeRepository.findAll();
    }

    public List<FuncionalidadeModel> listarPorApp(Integer appId) {
        return funcionalidadeRepository.findByAppPkApp(appId);
    }

    public Optional<FuncionalidadeModel> buscarPorId(Integer id) {
        return funcionalidadeRepository.findById(id);
    }

    public void removerFuncionalidade(Integer id) {
        funcionalidadeRepository.deleteById(id);
    }
}
