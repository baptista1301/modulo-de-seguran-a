package ucan.edu.moduloSeguranca.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.models.GrupoModel;
import ucan.edu.moduloSeguranca.repository.FuncionalidadeRepository;
import ucan.edu.moduloSeguranca.repository.GrupoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GrupoService {

        @Autowired
        private GrupoRepository grupoRepository;

        @Autowired
        private FuncionalidadeRepository funcionalidadeRepository;

        public GrupoModel criarGrupo(String designacao) {
            GrupoModel grupo = new GrupoModel();
            grupo.setDesignacao(designacao);
            return grupoRepository.save(grupo);
        }

        public List<GrupoModel> listarGrupos() {
            return grupoRepository.findAll();
        }

        public Optional<GrupoModel> buscarPorId(Integer id) {
            return grupoRepository.findById(id);
        }

        public GrupoModel adicionarFuncionalidadeAoGrupo(Integer grupoId, Integer funcId) {
            GrupoModel grupo = grupoRepository.findById(grupoId).orElseThrow();
            FuncionalidadeModel func = funcionalidadeRepository.findById(funcId).orElseThrow();
            grupo.getFuncionalidades().add(func);
            return grupoRepository.save(grupo);
        }

        public Set<FuncionalidadeModel> listarFuncionalidadesDoGrupo(Integer grupoId) {
            GrupoModel grupo = grupoRepository.findById(grupoId).orElseThrow();
            return grupo.getFuncionalidades();
        }
    }
