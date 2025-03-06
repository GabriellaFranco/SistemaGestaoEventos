package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.model.dto.CreateUsuarioDTO;
import com.enterprise.gestaoeventos.model.dto.GetUsuarioDTO;
import com.enterprise.gestaoeventos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<List<GetUsuarioDTO>> getAllUsuarios() {
        List<GetUsuarioDTO> usuarios = service.getAllUsuarios();
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findUsuarioById(id));
    }

    @PostMapping
    public ResponseEntity<GetUsuarioDTO> createUsuario(@RequestBody @Valid CreateUsuarioDTO usuarioDTO) {
        var usuario = service.createUsuario(usuarioDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        service.deleteUsuario(id);
        return ResponseEntity.ok("Usuário deletado com sucesso: " + id);
    }
}
