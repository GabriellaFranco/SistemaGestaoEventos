package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public record CreateEventoDTO(

        @NotNull @Size(min = 6, max = 30, message = "O nome deve ter ao menos 6 caracteres")
        String nome,

        @NotNull
        TipoEvento tipoEvento,

        @NotNull @Size(min = 20, max = 1000, message = "A descrição deve ter entre 20 e 1000 caracteres")
        String descricao,

        @NotNull @Future(message = "A data de início deve ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime dataInicio,

        @NotNull @Future(message = "A data de fim deve ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime dataFim,

        @NotNull @Size(min = 10, max = 100, message = "O local deve ter entre 20 e 100 caracteres")
        String local,

        @NotNull @Positive(message = "Número inválido")
        int capacidadeMaxima,

        @NotNull
        Long organizador_id

) {}
