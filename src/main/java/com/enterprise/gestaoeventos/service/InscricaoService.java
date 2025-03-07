package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.model.mapper.InscricaoMapper;
import com.enterprise.gestaoeventos.repository.InscricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class InscricaoService {

    private final InscricaoRepository repository;
    private final InscricaoMapper mapper;

    public List<GetInscricaoDTO> getAllInscricoes() {
        return repository.findAll().stream().map(mapper::toGetInscricaoDTO).toList();
    }

    public GetInscricaoDTO getInscricaoById(Long id) {
        return repository.findById(id).map(mapper::toGetInscricaoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada no Sistema: " + id));
    }

    public GetInscricaoDTO createInscricao(CreateInscricaoDTO inscricaoDTO) {
        var inscricaoMapped = mapper.toInscricao(inscricaoDTO);
        repository.save(inscricaoMapped);
        return mapper.toGetInscricaoDTO(inscricaoMapped);
    }

    public void deleteInscricao(Long id) {
        var inscricaoDeletada = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscrição não encontrada no Sistema: " + id));

        repository.delete(inscricaoDeletada);
    }
}
