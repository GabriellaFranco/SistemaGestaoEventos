package com.enterprise.gestaoeventos.model.mapper;

import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.model.entity.Inscricao;
import com.enterprise.gestaoeventos.model.entity.Pagamento;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PagamentoMapper {

    public Pagamento toPagamento(CreatePagamentoDTO pagamentoDTO) {
        return Pagamento.builder()
                .dataPagamento(pagamentoDTO.dataPagamento())
                .status(pagamentoDTO.statusPagamento())
                .valor(pagamentoDTO.valor())
                .build();
    }

    public GetPagamentoDTO toGetPagamentoDTO(Pagamento pagamento) {
        return GetPagamentoDTO.builder()
                .id(pagamento.getId())
                .dataPagamento(pagamento.getDataPagamento())
                .statusPagamento(pagamento.getStatus())
                .valor(pagamento.getValor())
                .inscricao(GetPagamentoDTO.InscricaoDTO.builder()
                        .id(pagamento.getInscricao().getId())
                        .statusInscricao(pagamento.getInscricao().getStatusInscricao())
                        .build())
                .build();
    }
}
