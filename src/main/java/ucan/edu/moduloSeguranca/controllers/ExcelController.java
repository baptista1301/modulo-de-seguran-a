package ucan.edu.moduloSeguranca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ucan.edu.moduloSeguranca.models.AppModel;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.repository.AppRepository;
import ucan.edu.moduloSeguranca.repository.FuncionalidadeRepository;
import ucan.edu.moduloSeguranca.services.ExcelService;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;


    @Autowired
    private AppRepository appRepository;

    @Autowired
    private FuncionalidadeRepository funcionalidadeRepository;

    @PostMapping("/import")
    public ResponseEntity<String> importarExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importarExcel(file);
            return ResponseEntity.ok("Arquivo Funcionalidade importado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao importar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/teste-salvar")
    public String testeSalvar() {
        try {
            // Criar e salvar App
            AppModel app = new AppModel();
            app.setNome("Teste");
            app.setDescricao("Teste");
            appRepository.save(app);

            // Criar e salvar Funcionalidade
            FuncionalidadeModel f = new FuncionalidadeModel();
            f.setDesignacao("Func Teste");
            f.setDescricao("Funcionalidade teste");
            f.setApp(app);
            funcionalidadeRepository.save(f);

            return "App salvo com ID: " + app.getPkApp() + " | Funcionalidade salva com ID: " + f.getPkFuncionalidade();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao salvar: " + e.getMessage();
        }
    }
}
