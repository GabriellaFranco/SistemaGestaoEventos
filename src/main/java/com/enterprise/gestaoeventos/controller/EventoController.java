package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.model.dto.CreateEventoDTO;
import com.enterprise.gestaoeventos.model.dto.GetEventoDTO;
import com.enterprise.gestaoeventos.service.EventoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/eventos")
@Tag(name = "Eventos", description = "API para gerenciamento de eventos")
public class EventoController {

    private final EventoService service;

    @GetMapping
    public ResponseEntity<List<GetEventoDTO>> getAllEventos() {
        List<GetEventoDTO> eventos = service.getAllEventos();
        return eventos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEventoDTO> getEventoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEventoById(id));
    }

    @PostMapping
    public ResponseEntity<GetEventoDTO> createEvento(@RequestBody @Valid CreateEventoDTO eventoDTO,
                                                     @AuthenticationPrincipal UserDetails userDetails) {

        var eventoCriado = service.createEvento(eventoDTO, userDetails.getUsername());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(eventoCriado.id()).toUri();

        return ResponseEntity.created(uri).body(eventoCriado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvento(@PathVariable Long id) {
        service.deleteEvento(id);
        return ResponseEntity.ok("Evento deletado com sucesso: " + id);
    }
}
