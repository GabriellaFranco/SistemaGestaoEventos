package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateEventoDTO;
import com.enterprise.gestaoeventos.model.dto.GetEventoDTO;
import com.enterprise.gestaoeventos.model.mapper.EventoMapper;
import com.enterprise.gestaoeventos.repository.EventoRepository;
import com.enterprise.gestaoeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class EventoService {

    private final EventoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final EventoMapper mapper;

    public List<GetEventoDTO> getAllEventos() {
        return repository.findAll().stream().map(mapper::toGetEventoDTO).toList();
    }

    public GetEventoDTO getEventoById(Long id) {
        return repository.findById(id).map(mapper::toGetEventoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado no Sistema: " + id));
    }

    public GetEventoDTO createEvento(CreateEventoDTO eventoDTO, String usuarioEmail) {
        var organizador = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado no sistema: " + usuarioEmail));

        var eventoMapped = mapper.toEvento(eventoDTO, organizador);
        repository.save(eventoMapped);
        return mapper.toGetEventoDTO(eventoMapped);
    }

    public void deleteEvento(Long id) {
        var eventoDelete = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado no Sistema: " + id));
        repository.delete(eventoDelete);
    }
}
