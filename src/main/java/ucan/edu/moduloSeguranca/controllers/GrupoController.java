package ucan.edu.moduloSeguranca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.models.GrupoModel;
import ucan.edu.moduloSeguranca.services.GrupoService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoModel> criarGrupo(@RequestParam String designacao) {
        GrupoModel grupo = grupoService.criarGrupo(designacao);
        return ResponseEntity.ok(grupo);
    }

    @GetMapping
    public ResponseEntity<List<GrupoModel>> listarGrupos() {
        return ResponseEntity.ok(grupoService.listarGrupos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoModel> buscarPorId(@PathVariable Integer id) {
        return grupoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{grupoId}/funcionalidades/{funcId}")
    public ResponseEntity<GrupoModel> adicionarFuncionalidade(@PathVariable Integer grupoId, @PathVariable Integer funcId) {
        GrupoModel grupo = grupoService.adicionarFuncionalidadeAoGrupo(grupoId, funcId);
        return ResponseEntity.ok(grupo);
    }

    @GetMapping("/{grupoId}/funcionalidades")
    public ResponseEntity<Set<FuncionalidadeModel>> listarFuncionalidades(@PathVariable Integer grupoId) {
        return ResponseEntity.ok(grupoService.listarFuncionalidadesDoGrupo(grupoId));
    }
}
