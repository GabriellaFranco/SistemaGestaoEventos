package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateUsuarioDTO;
import com.enterprise.gestaoeventos.model.dto.GetUsuarioDTO;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.model.mapper.UsuarioMapper;
import com.enterprise.gestaoeventos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private GetUsuarioDTO getUsuarioDTO;
    private CreateUsuarioDTO createUsuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@30")
                .build();

        getUsuarioDTO = GetUsuarioDTO.builder()
                .id(1L)
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@30")
                .role(Role.ROLE_PARTICIPANTE)
                .build();

        createUsuarioDTO = CreateUsuarioDTO.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@30")
                .role(Role.ROLE_PARTICIPANTE)
                .build();
    }

    @Test
    void testUsuarioService_WhenCalledMethodGetAllUsuarios_ShouldReturnListOfUsuarioObject() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        when(mapper.toGetUsuarioDTO(usuario)).thenReturn(getUsuarioDTO);

        List<GetUsuarioDTO> usuarios = usuarioService.getAllUsuarios();

        assertEquals(1, usuarios.size());
        assertEquals("liana@gmail.com", usuarios.get(0).email());
    }

    @Test
    void testUsuarioService_WhenCalledFindUsuarioById_ShouldReturnUsuarioObject() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(mapper.toGetUsuarioDTO(usuario)).thenReturn(getUsuarioDTO);

        var usuarioEncontrado = usuarioService.findUsuarioById(1L);

        assertThat(usuarioEncontrado).isNotNull();
        assertThat(usuarioEncontrado.nome()).isEqualTo(usuario.getNome());
    }

    @Test
    void testUsuarioService_WhenCalledFindUsuarioWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findUsuarioById(2L));
    }

    @Test
    void testUsuarioService_WhenCalledCreateUsuario_ShouldPersistAndReturnUsuarioObject() {
        when(mapper.toUsuario(createUsuarioDTO)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toGetUsuarioDTO(usuario)).thenReturn(getUsuarioDTO);

        GetUsuarioDTO usuarioCriado = usuarioService.createUsuario(createUsuarioDTO);

        assertThat(usuarioCriado).isNotNull();
        assertThat(usuarioCriado.nome()).isEqualTo(createUsuarioDTO.nome());
    }

    @Test
    void testUsuarioService_WhenCalledDeleteUsuario_ShouldReturnNothing() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deleteUsuario(1L);

        verify(repository, times(1)).delete(any());
    }

    @Test
    void testUsuarioService_WhenCalledDeleteUsuarioWithInexistentID_ShouldThrowResourceNotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deleteUsuario(2L));
    }
}

