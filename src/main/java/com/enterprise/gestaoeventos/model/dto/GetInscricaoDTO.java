package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record GetInscricaoDTO(
       Long id,
       LocalDateTime dataInscricao,
       StatusInscricao statusInscricao,
       UsuarioDTO usuario,
       EventoDTO evento
) {

    @Builder
    public record UsuarioDTO(
            Long id,
            String nome
    ) {}

    @Builder
    public record EventoDTO(
            String nome,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    ) {}

}
