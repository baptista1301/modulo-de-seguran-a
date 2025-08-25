package ucan.edu.moduloSeguranca.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app")
@Builder
public class AppModel
{
    @Id
    @Column(name = "pk_app")
    private Integer pkApp;

    private String nome;
    private String descricao;
    @Builder.Default
    private LocalDateTime data = LocalDateTime.now();
}
