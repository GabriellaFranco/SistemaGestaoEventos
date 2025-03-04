package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetUsuarioDTO(
        Long id,
        String email,
        String nome,
        String senha,
        Role role,
        List<EventoDTO> eventosOrganizados,
        List<InscricaoDTO> inscricoes
) {

    @Builder
    public record EventoDTO(
            Long id,
            String nome,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    ) {}

    @Builder
    public record InscricaoDTO(
            Long id,
            StatusInscricao statusInscricao
    ) {}
}
