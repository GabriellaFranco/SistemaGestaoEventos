package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateUsuarioDTO;
import com.enterprise.gestaoeventos.model.dto.GetUsuarioDTO;
import com.enterprise.gestaoeventos.model.mapper.UsuarioMapper;
import com.enterprise.gestaoeventos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder encoder;

    public List<GetUsuarioDTO> getAllUsuarios() {
        return repository.findAll().stream().map(mapper::toGetUsuarioDTO).toList();
    }

    public GetUsuarioDTO findUsuarioById(Long id) {
        return repository.findById(id)
                .map(mapper::toGetUsuarioDTO).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    public GetUsuarioDTO createUsuario(CreateUsuarioDTO usuarioDTO) {
        var usuarioMapped = mapper.toUsuario(usuarioDTO);
        usuarioMapped.setSenha(encoder.encode(usuarioMapped.getSenha()));
        usuarioMapped.setAtivo(true);
        repository.save(usuarioMapped);
        return mapper.toGetUsuarioDTO(usuarioMapped);
    }

    public void deleteUsuario(Long id) {
        var usuarioDelete = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));

        repository.delete(usuarioDelete);
    }
}
