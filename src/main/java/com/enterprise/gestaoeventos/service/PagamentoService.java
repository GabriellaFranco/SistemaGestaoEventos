package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.model.mapper.PagamentoMapper;
import com.enterprise.gestaoeventos.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoMapper mapper;
    private final PagamentoRepository repository;

    public List<GetPagamentoDTO> getAllPagamentos() {
        return repository.findAll().stream().map(mapper::toGetPagamentoDTO).toList();
    }

    public GetPagamentoDTO getPagamentoById(Long id) {
        return repository.findById(id).map(mapper::toGetPagamentoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado no Sistema:" + id));
    }

    public GetPagamentoDTO createPagamento(CreatePagamentoDTO pagamentoDTO) {
        var pagamentoMapper = mapper.toPagamento(pagamentoDTO);
        repository.save(pagamentoMapper);
        return mapper.toGetPagamentoDTO(pagamentoMapper);
    }

    public void deletePagamento(Long id) {
        var pagamentoDeletado = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado no Sistema:" + id));

        repository.delete(pagamentoDeletado);
    }
}
