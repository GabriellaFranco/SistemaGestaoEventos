package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.service.InscricaoService;
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
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService service;

    @GetMapping
    public ResponseEntity<List<GetInscricaoDTO>> getAllInscricoes() {
        List<GetInscricaoDTO> inscricoes = service.getAllInscricoes();
        return inscricoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetInscricaoDTO> getInscricaoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInscricaoById(id));
    }

    @PostMapping
    public ResponseEntity<GetInscricaoDTO> createInscricao(@RequestBody @Valid CreateInscricaoDTO inscricaoDTO,
                                                           @AuthenticationPrincipal UserDetails userDetails) {

        var inscricao = service.createInscricao(inscricaoDTO, userDetails.getUsername());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(inscricao.id()).toUri();
        return ResponseEntity.created(uri).body(inscricao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInscricao(@PathVariable Long id) {
        service.deleteInscricao(id);
        return ResponseEntity.ok("Inscricao deletada com sucesso: " + id);
    }
}
