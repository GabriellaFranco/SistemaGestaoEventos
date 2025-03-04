package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record GetPagamentoDTO(
        Long id,
        LocalDateTime dataPagamento,
        BigDecimal valor,
        StatusPagamento statusPagamento,
        InscricaoDTO inscricao
) {

    @Builder
    public record InscricaoDTO(
            Long id,
            StatusInscricao statusInscricao
    ) {}
}
