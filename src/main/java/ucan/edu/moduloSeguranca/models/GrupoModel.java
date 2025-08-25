package ucan.edu.moduloSeguranca.models;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "grupo")
public class GrupoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_grupo")
    private Integer pkGrupo;

    @Column(name = "designacao", nullable = false)
    private String designacao;

    @ManyToMany
    @JoinTable(
            name = "grupo_funcionalidade",
            joinColumns = @JoinColumn(name = "fk_grupo"),
            inverseJoinColumns = @JoinColumn(name = "fk_funcionalidade")
    )
    private Set<FuncionalidadeModel> funcionalidades = new HashSet<>();

    public GrupoModel() {}

    public GrupoModel(Integer pkGrupo, String designacao) {
        this.pkGrupo = pkGrupo;
        this.designacao = designacao;
    }

    public Integer getPkGrupo() {
        return pkGrupo;
    }

    public void setPkGrupo(Integer pkGrupo) {
        this.pkGrupo = pkGrupo;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public Set<FuncionalidadeModel> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(Set<FuncionalidadeModel> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }
}
