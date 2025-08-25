package ucan.edu.moduloSeguranca.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_funcionalidade")
public class TipoFuncionalidadeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_tipo_funcionalidade;

    @Column(nullable = false)
    private String designacao;

    public Integer getPk_tipo_funcionalidade() {
        return pk_tipo_funcionalidade;
    }

    public void setPk_tipo_funcionalidade(Integer pk_tipo_funcionalidade) {
        this.pk_tipo_funcionalidade = pk_tipo_funcionalidade;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }
}
