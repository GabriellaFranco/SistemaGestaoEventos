package com.enterprise.gestaoeventos.model.entity;

import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
@Entity
@Table(name = "inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    @Enumerated(EnumType.STRING)
    private StatusInscricao statusInscricao;

    @OneToOne(mappedBy = "inscricao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

}
