package com.enterprise.gestaoeventos.model.mapper;

import com.enterprise.gestaoeventos.model.dto.CreateEventoDTO;
import com.enterprise.gestaoeventos.model.dto.GetEventoDTO;
import com.enterprise.gestaoeventos.model.entity.Evento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {

    public Evento toEvento(CreateEventoDTO eventoDTO) {
        return Evento.builder()
                .nome(eventoDTO.nome())
                .tipoEvento(eventoDTO.tipoEvento())
                .dataInicio(eventoDTO.dataInicio())
                .dataFim(eventoDTO.dataFim())
                .local(eventoDTO.local())
                .descricao(eventoDTO.descricao())
                .capacidadeMaxima(eventoDTO.capacidadeMaxima())
                .build();
    }

    public GetEventoDTO toGetEventoDTO(Evento evento) {
        return GetEventoDTO.builder()
                .id(evento.getId())
                .tipoEvento(evento.getTipoEvento())
                .nome(evento.getNome())
                .local(evento.getLocal())
                .dataInicio(evento.getDataInicio())
                .dataFim(evento.getDataFim())
                .descricao(evento.getDescricao())
                .capacidadeMaxima(evento.getCapacidadeMaxima())
                .organizador(GetEventoDTO.UsuarioDTO.builder()
                        .id(evento.getOrganizador().getId())
                        .nome(evento.getOrganizador().getNome())
                        .build())
                .inscricoes(evento.getInscricoes().stream().map(inscricao -> GetEventoDTO.InscricaoDTO.builder()
                        .id(inscricao.getId())
                        .statusInscricao(inscricao.getStatusInscricao())
                        .build()).toList())
                .build();
    }
}
