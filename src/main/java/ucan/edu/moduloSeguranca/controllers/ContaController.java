package ucan.edu.moduloSeguranca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.moduloSeguranca.models.ContaModel;
import ucan.edu.moduloSeguranca.services.ContaService;

import java.util.Map;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam Integer grupoId) {
        ContaModel conta = contaService.criarConta(username, password, grupoId);
        return ResponseEntity.ok(conta);
    }

}
