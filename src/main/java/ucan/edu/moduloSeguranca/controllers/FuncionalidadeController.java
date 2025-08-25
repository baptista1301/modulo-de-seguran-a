package ucan.edu.moduloSeguranca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.services.FuncionalidadeService;

import java.util.List;

@RestController
@RequestMapping("/funcionalidades")
public class FuncionalidadeController {

    @Autowired
    private FuncionalidadeService funcionalidadeService;

    @PostMapping("/app/{appId}")
    public ResponseEntity<FuncionalidadeModel> criarFuncionalidade(@PathVariable Integer appId,
                                                                   @RequestBody FuncionalidadeModel funcionalidade) {
        FuncionalidadeModel criada = funcionalidadeService.criarFuncionalidade(funcionalidade, appId);
        return ResponseEntity.ok(criada);
    }

    @GetMapping
    public ResponseEntity<List<FuncionalidadeModel>> listarTodas() {
        return ResponseEntity.ok(funcionalidadeService.listarTodas());
    }

    @GetMapping("/app/{appId}")
    public ResponseEntity<List<FuncionalidadeModel>> listarPorApp(@PathVariable Integer appId) {
        return ResponseEntity.ok(funcionalidadeService.listarPorApp(appId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionalidadeModel> buscarPorId(@PathVariable Integer id) {
        return funcionalidadeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Integer id) {
        funcionalidadeService.removerFuncionalidade(id);
        return ResponseEntity.ok("Funcionalidade removida com sucesso.");
    }
}
