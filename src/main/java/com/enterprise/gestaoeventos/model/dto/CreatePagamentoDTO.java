package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CreatePagamentoDTO(

        @Nullable @FutureOrPresent(message = "Data de pagamento inválida")
        LocalDateTime dataPagamento,

        @NotNull @Positive(message = "Valor informado inválido")
        BigDecimal valor,

        @NotNull
        StatusPagamento statusPagamento,

        @NotNull
        Long inscricaoId

) {}
