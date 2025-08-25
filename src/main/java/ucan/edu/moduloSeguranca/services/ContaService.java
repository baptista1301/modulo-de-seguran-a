package ucan.edu.moduloSeguranca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.moduloSeguranca.models.ContaModel;
import ucan.edu.moduloSeguranca.models.GrupoModel;
import ucan.edu.moduloSeguranca.repository.ContaRepository;
import ucan.edu.moduloSeguranca.repository.GrupoRepository;

import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    public ContaModel criarConta(String username, String password, Integer grupoId) {
        GrupoModel grupo = grupoRepository.findById(grupoId).orElseThrow();
        ContaModel conta = ContaModel.builder()
                .username(username)
                .password(password) // Em produção, usar BCrypt
                .grupo(grupo)
                .build();
        return contaRepository.save(conta);
    }

    public Optional<ContaModel> autenticar(String username, String password) {
        Optional<ContaModel> contaOpt = contaRepository.findByUsername(username);
        if (contaOpt.isPresent() && contaOpt.get().getPassword().equals(password)) {
            return contaOpt;
        }
        return Optional.empty();
    }
}
