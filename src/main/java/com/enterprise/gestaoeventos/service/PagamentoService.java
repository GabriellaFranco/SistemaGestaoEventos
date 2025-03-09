package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.model.mapper.PagamentoMapper;
import com.enterprise.gestaoeventos.repository.InscricaoRepository;
import com.enterprise.gestaoeventos.repository.PagamentoRepository;
import com.enterprise.gestaoeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoMapper mapper;
    private final PagamentoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final InscricaoRepository inscricaoRepository;

    public List<GetPagamentoDTO> getAllPagamentos() {
        return repository.findAll().stream().map(mapper::toGetPagamentoDTO).toList();
    }

    public GetPagamentoDTO getPagamentoById(Long id) {
        return repository.findById(id).map(mapper::toGetPagamentoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado no Sistema:" + id));
    }

    public GetPagamentoDTO createPagamento(CreatePagamentoDTO pagamentoDTO, String userEmail) {
        var usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado no Sistema: " + userEmail));

        var inscricao = inscricaoRepository.findById(pagamentoDTO.inscricaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Inscricao não encontrada no Sistema: " + pagamentoDTO.inscricaoId()));

        var pagamentoMapper = mapper.toPagamento(pagamentoDTO, inscricao, usuario);
        pagamentoMapper.setStatus(StatusPagamento.PENDENTE);
        repository.save(pagamentoMapper);
        return mapper.toGetPagamentoDTO(pagamentoMapper);
    }

    public void deletePagamento(Long id) {
        var pagamentoDeletado = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado no Sistema:" + id));

        repository.delete(pagamentoDeletado);
    }
}
