package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetEventoDTO(
        Long id,
        String nome,
        TipoEvento tipoEvento,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String local,
        int capacidadeMaxima,
        UsuarioDTO organizador,
        List<InscricaoDTO> inscricoes
) {

    @Builder
    public record UsuarioDTO(
            Long id,
            String nome
    ) {}

    @Builder
    public record InscricaoDTO(
            Long id,
            StatusInscricao statusInscricao
    ) {}
}
