package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.service.PagamentoService;
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
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    @GetMapping
    public ResponseEntity<List<GetPagamentoDTO>> getAllPagamentos() {
        List<GetPagamentoDTO> pagamentos = service.getAllPagamentos();
        return pagamentos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPagamentoDTO> getPagamentoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPagamentoById(id));
    }

    @PostMapping
    public ResponseEntity<GetPagamentoDTO> createPagamento(@RequestBody @Valid CreatePagamentoDTO pagamentoDTO,
                                                           @AuthenticationPrincipal UserDetails userDetails) {

        var pagamento = service.createPagamento(pagamentoDTO, userDetails.getUsername());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(pagamento.id()).toUri();
        return ResponseEntity.created(uri).body(pagamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePagamento(@PathVariable Long id) {
        service.deletePagamento(id);
        return ResponseEntity.ok("Pagamento deletado com sucesso: " + id);
    }
}
