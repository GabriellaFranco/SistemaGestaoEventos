package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateInscricaoDTO(

        @NotNull @FutureOrPresent
        LocalDateTime dataInscricao,

        @NotNull
        StatusInscricao statusInscricao,

        @NotNull
        Long eventoId,

        @NotNull
        StatusPagamento statusPagamento
) {}
