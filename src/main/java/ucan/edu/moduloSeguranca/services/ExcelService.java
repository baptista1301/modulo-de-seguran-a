package ucan.edu.moduloSeguranca.services;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ucan.edu.moduloSeguranca.models.AppModel;
import ucan.edu.moduloSeguranca.models.FuncionalidadeModel;
import ucan.edu.moduloSeguranca.models.TipoFuncionalidadeModel;
import ucan.edu.moduloSeguranca.repository.AppRepository;
import ucan.edu.moduloSeguranca.repository.FuncionalidadeRepository;
import ucan.edu.moduloSeguranca.repository.TipoFuncionalidadeRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelService.class);

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private FuncionalidadeRepository funcionalidadeRepository;

    @Autowired
    private TipoFuncionalidadeRepository tipoFuncionalidadeRepository;

    @Transactional
    public void importarExcel(MultipartFile file) throws IOException {
        log.info("Iniciando importação do arquivo Excel: {}", file.getOriginalFilename());

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet tiposSheet = workbook.getSheet("tipo_funcionalidade");
            if (tiposSheet != null) {
                importarTipos(tiposSheet);
            } else {
                log.warn("Aba 'tipo_funcionalidade' não encontrada!");
            }

            Sheet funcionalidadesSheet = workbook.getSheet("funcionalidade");
            if (funcionalidadesSheet != null) {
                importarAppEFuncionalidades(funcionalidadesSheet);
            } else {
                log.warn("Aba 'funcionalidade' não encontrada!");
            }
        }

        log.info("Arquivo {} importado com sucesso!", file.getOriginalFilename());
    }

    /**
     * ================= APP + FUNCIONALIDADES =================
     */
    private void importarAppEFuncionalidades(Sheet sheet) {
        Map<String, String> dadosApp = new HashMap<>();
        int linhaFuncionalidades = -1;

        // === 1º passo: Ler dados da aplicação (vertical) ===
        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || row.getPhysicalNumberOfCells() < 2) continue;

            String chave = getCellAsString(row.getCell(0));
            String valor = getCellAsString(row.getCell(1));
            if (chave != null && valor != null) {
                dadosApp.put(norm(chave), valor);
            }

            if (norm(chave).equals("pk_funcionalidade")) {
                linhaFuncionalidades = r;
                break;
            }
        }

        if (!dadosApp.containsKey("pk_app")) {
            log.error("pk_app não encontrado nos dados verticais da aplicação.");
            return;
        }

        AppModel app = new AppModel();
        app.setPkApp(Integer.valueOf(dadosApp.get("pk_app")));
        app.setNome(dadosApp.get("nome"));
        app.setDescricao(dadosApp.get("descricao"));
        app.setData(parseDateLeniente(dadosApp.get("data")));

        try {
            app = appRepository.saveAndFlush(app);
            log.info("Aplicação salva: {}", app.getNome());
        } catch (Exception e) {
            log.error("Erro ao salvar app: {}", e.getMessage(), e);
        }

        // === 2º passo: Ler funcionalidades (horizontal) ===
        Row header = sheet.getRow(linhaFuncionalidades);
        Map<String, Integer> idx = mapearCabecalhos(header);

        Map<Integer, FuncionalidadeModel> cache = new HashMap<>();
        Map<Integer, String> paiMap = new HashMap<>();
        Map<Integer, String> compartilhadasMap = new HashMap<>();

        for (int r = linhaFuncionalidades + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            String idStr = getByHeader(row, idx, "pk_funcionalidade");
            if (idStr == null || idStr.isBlank() || !idStr.matches("\\d+")) continue;
            Integer pkFuncionalidade = Integer.valueOf(idStr);

            String designacao = getByHeader(row, idx, "designacao");
            String descricao = getByHeader(row, idx, "descricao");
            String url = getByHeader(row, idx, "url");
            String tipoStr = getByHeader(row, idx, "fk_tipo_funcionalidade");
            String paiStr = getByHeader(row, idx, "fk_funcionalidade");
            String compsStr = getByHeader(row, idx, "funcionalidades_partilhadas");

            TipoFuncionalidadeModel tipo = null;
            if (tipoStr != null && !tipoStr.isBlank()) {
                try {
                    Integer tipoId = Integer.valueOf(tipoStr);
                    tipo = tipoFuncionalidadeRepository.findById(tipoId).orElse(null);
                } catch (NumberFormatException e) {
                    tipo = tipoFuncionalidadeRepository.findByDesignacao(tipoStr).orElse(null);
                }
            }

            FuncionalidadeModel func = new FuncionalidadeModel();
            func.setPkFuncionalidade(pkFuncionalidade);
            func.setDesignacao(designacao);
            func.setDescricao(descricao);
            func.setUrl(url);
            func.setTipoFuncionalidade(tipo);
            func.setApp(app);

            try {
                func = funcionalidadeRepository.saveAndFlush(func);
                cache.put(pkFuncionalidade, func);

                if (paiStr != null && !paiStr.isBlank()) paiMap.put(pkFuncionalidade, paiStr);
                if (compsStr != null && !compsStr.isBlank()) compartilhadasMap.put(pkFuncionalidade, compsStr);

                log.info("Funcionalidade salva: {}", designacao);
            } catch (Exception e) {
                log.error("Erro ao salvar funcionalidade '{}': {}", designacao, e.getMessage());
            }
        }

        // === 3º passo: Vincular pai ===
        for (Map.Entry<Integer, String> entry : paiMap.entrySet()) {
            Integer pk = entry.getKey();
            String paiStr = entry.getValue();
            FuncionalidadeModel func = cache.get(pk);
            if (func != null) {
                try {
                    Integer pkPai = Integer.valueOf(paiStr);
                    FuncionalidadeModel pai = cache.get(pkPai);
                    if (pai != null) func.setFuncionalidadePai(pai);
                    funcionalidadeRepository.saveAndFlush(func);
                } catch (Exception e) {
                    log.error("Erro ao vincular pai da funcionalidade '{}': {}", func.getDesignacao(), e.getMessage());
                }
            }
        }

        // === 4º passo: Vincular compartilhadas ===
        for (Map.Entry<Integer, String> entry : compartilhadasMap.entrySet()) {
            Integer pk = entry.getKey();
            String compsStr = entry.getValue();
            FuncionalidadeModel func = cache.get(pk);
            if (func != null) {
                try {
                    Set<FuncionalidadeModel> partilhadas = Arrays.stream(compsStr.split(";"))
                            .map(String::trim)
                            .map(Integer::valueOf)
                            .map(cache::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    func.getFuncionalidadesPartilhadas().addAll(partilhadas);
                    funcionalidadeRepository.saveAndFlush(func);
                } catch (Exception e) {
                    log.error("Erro ao salvar Partilhadas da '{}': {}", func.getDesignacao(), e.getMessage());
                }
            }
        }
    }

    /**
     * ================= TIPOS =================
     */
    private void importarTipos(Sheet sheet) {
        Row header = sheet.getRow(0);
        Map<String, Integer> idx = mapearCabecalhos(header);

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            String designacao = getByHeader(row, idx, "designacao");
            if (designacao == null || designacao.isBlank()) continue;

            TipoFuncionalidadeModel tipo = new TipoFuncionalidadeModel();
            tipo.setDesignacao(designacao);

            try {
                tipoFuncionalidadeRepository.saveAndFlush(tipo);
            } catch (Exception e) {
                log.error("Erro ao salvar tipo '{}': {}", designacao, e.getMessage());
            }
        }
    }

    /**
     * ================= HELPERS =================
     */
    private String norm(String s) {
        return s == null ? null : s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "_");
    }

    private Map<String, Integer> mapearCabecalhos(Row headerRow) {
        Map<String, Integer> idx = new HashMap<>();
        if (headerRow == null) return idx;
        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            idx.put(norm(getCellAsString(headerRow.getCell(c))), c);
        }
        return idx;
    }

    private String getByHeader(Row row, Map<String, Integer> idx, String header) {
        Integer col = idx.get(norm(header));
        return (col == null) ? null : getCellAsString(row.getCell(col));
    }

    private LocalDateTime parseDateLeniente(String raw) {
        if (raw == null || raw.isBlank()) return LocalDateTime.now();

        List<DateTimeFormatter> formatos = List.of(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ISO_DATE
        );

        for (DateTimeFormatter formato : formatos) {
            try {
                return LocalDateTime.parse(raw.replace('T', ' '), formato);
            } catch (DateTimeParseException ignored) {}
        }

        // Se não conseguir interpretar, retorna a data atual
        return LocalDateTime.now();
    }

    private String getCellAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> (DateUtil.isCellDateFormatted(cell)) ?
                    cell.getDateCellValue().toString() :
                    String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> "";
        };
    }
}