    package ucan.edu.moduloSeguranca.models;

    import jakarta.persistence.*;
    import lombok.*;
    import java.util.HashSet;
    import java.util.Set;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "funcionalidade")
    public class FuncionalidadeModel {

        @Id
        @Column(name = "pk_funcionalidade")
        private Integer pkFuncionalidade;

        @Column(name = "designacao")
        private String designacao;

        @Column(name = "descricao")
        private String descricao;

        @ManyToOne
        @JoinColumn(name = "fk_tipo_funcionalidade")
        private TipoFuncionalidadeModel tipoFuncionalidade;

        @ManyToOne
        @JoinColumn(name = "fk_grupo")
        private GrupoModel grupo;

        @ManyToOne
        @JoinColumn(name = "fk_funcionalidade_pai")
        private FuncionalidadeModel funcionalidadePai;

        @Column(name = "url")
        private String url;

        @ManyToOne
        @JoinColumn(name = "fk_app")
        private AppModel app;

        // 🔹 Relacionamento N:N (funcionalidades partilhadas)
        @ManyToMany
        @JoinTable(
                name = "funcionalidade_partilhada",
                joinColumns = @JoinColumn(name = "fk_funcionalidade_origem"),
                inverseJoinColumns = @JoinColumn(name = "fk_funcionalidade_destino")
        )
        private Set<FuncionalidadeModel> funcionalidadesPartilhadas = new HashSet<>();

        // 🔹 Construtor de conveniência (caso precises instanciar manualmente)
        public FuncionalidadeModel(String designacao, String descricao, String url) {
            this.designacao = designacao;
            this.descricao = descricao;
            this.url = url;
        }
    }
