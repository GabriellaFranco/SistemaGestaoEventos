package com.enterprise.gestaoeventos.model.mapper;

import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.entity.Inscricao;
import com.enterprise.gestaoeventos.model.entity.Pagamento;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class InscricaoMapper {

    public Inscricao toInscricao(CreateInscricaoDTO inscricaoDTO, Evento evento, Usuario usuario) {
        return Inscricao.builder()
                .evento(evento)
                .usuario(usuario)
                .build();
    }

    public GetInscricaoDTO toGetInscricaoDTO(Inscricao inscricao) {
        return GetInscricaoDTO.builder()
                .id(inscricao.getId())
                .dataInscricao(inscricao.getDataInscricao())
                .statusInscricao(inscricao.getStatusInscricao())
                .evento(GetInscricaoDTO.EventoDTO.builder()
                        .nome(inscricao.getEvento().getNome())
                        .dataInicio(inscricao.getEvento().getDataInicio())
                        .dataFim(inscricao.getEvento().getDataFim())
                        .build())
                .usuario(GetInscricaoDTO.UsuarioDTO.builder()
                        .id(inscricao.getUsuario().getId())
                        .nome(inscricao.getUsuario().getNome())
                        .build())
                .build();
    }
}
