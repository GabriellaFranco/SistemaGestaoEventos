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

    public Inscricao toInscricao(CreateInscricaoDTO inscricaoDTO, Evento evento, Pagamento pagamento, Usuario usuario) {
        return Inscricao.builder()
                .dataInscricao(inscricaoDTO.dataInscricao())
                .statusInscricao(inscricaoDTO.statusInscricao())
                .evento(evento)
                .pagamento(pagamento)
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
                .pagamento(GetInscricaoDTO.PagamentoDTO.builder()
                        .id(inscricao.getPagamento().getId())
                        .valor(inscricao.getPagamento().getValor())
                        .dataPagamento(inscricao.getPagamento().getDataPagamento())
                        .statusPagamento(inscricao.getPagamento().getStatus())
                        .build())
                .build();
    }
}
