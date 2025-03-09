package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.mapper.InscricaoMapper;
import com.enterprise.gestaoeventos.repository.EventoRepository;
import com.enterprise.gestaoeventos.repository.InscricaoRepository;
import com.enterprise.gestaoeventos.repository.PagamentoRepository;
import com.enterprise.gestaoeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class InscricaoService {

    private final InscricaoRepository repository;
    private final InscricaoMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final PagamentoRepository pagamentoRepository;

    public List<GetInscricaoDTO> getAllInscricoes() {
        return repository.findAll().stream().map(mapper::toGetInscricaoDTO).toList();
    }

    public GetInscricaoDTO getInscricaoById(Long id) {
        return repository.findById(id).map(mapper::toGetInscricaoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada no Sistema: " + id));
    }

    public GetInscricaoDTO createInscricao(CreateInscricaoDTO inscricaoDTO, String usuarioEmail) {

        var usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado no Sistema: " + usuarioEmail));

        var evento = eventoRepository.findById(inscricaoDTO.eventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado no Sistema: " + inscricaoDTO.eventoId()));

        var inscricaoMapped = mapper.toInscricao(inscricaoDTO, evento, usuario);
        inscricaoMapped.setDataInscricao(LocalDateTime.now());
        inscricaoMapped.setStatusInscricao(StatusInscricao.PENDENTE);
        repository.save(inscricaoMapped);
        return mapper.toGetInscricaoDTO(inscricaoMapped);
    }

    public void deleteInscricao(Long id) {
        var inscricaoDeletada = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada no Sistema: " + id));

        repository.delete(inscricaoDeletada);
    }
}
