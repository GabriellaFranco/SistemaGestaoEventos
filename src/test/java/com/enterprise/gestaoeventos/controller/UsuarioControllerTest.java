package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateUsuarioDTO;
import com.enterprise.gestaoeventos.model.dto.GetUsuarioDTO;
import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private GetUsuarioDTO usuario1;
    private GetUsuarioDTO usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = GetUsuarioDTO.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@2077")
                .role(Role.ROLE_PARTICIPANTE)
                .build();

        usuario2 = GetUsuarioDTO.builder()
                .nome("Gabriella")
                .email("gabriella@gmail.com")
                .senha("Gabi@2077")
                .role(Role.ROLE_ORGANIZADOR)
                .build();
    }

    @Test
    void testUsuarioCOntroller_WhenCalledGetAllUsuarios_ShouldReturnListOfUsuarioObject() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario1, usuario2));

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value("Gabriella"));

    }

    @Test
    void testUsuarioCOntroller_WhenCalledGetAllUsuariosWithNoUsuarios_ShouldReturnEmptyList() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUsuarioController_WhenCalledFindUsuarioById_ShouldReturnUsuarioObject() throws Exception {
        when(usuarioService.findUsuarioById(1L)).thenReturn(usuario1);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Liana"));
    }

    @Test
    void testUsuarioController_WhenCalledFindUsuarioByIdWithInexistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(usuarioService.findUsuarioById(10L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUsuarioController_WhenCalledCreateUsuario_ShouldPersistAndReturnUsuarioObject() throws Exception {

        var createDto = CreateUsuarioDTO.builder()
                .nome("Cristiane")
                .email("cristiane@gmail.com")
                .senha("Cristiane@2077")
                .role(Role.ROLE_ADMIN)
                .build();

        var usuarioCriado = GetUsuarioDTO.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@2077")
                .role(Role.ROLE_PARTICIPANTE)
                .build();

        when(usuarioService.createUsuario(any())).thenReturn(usuarioCriado);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Liana"));
    }

    @Test
    void testUsuarioController_WhenCalledCreateUsuarioWithoutEmail_ShouldReturnBadRequest() throws Exception {

        var createDto = CreateUsuarioDTO.builder()
                .nome("Cristiane")
                .senha("Cristiane@2077")
                .role(Role.ROLE_ADMIN)
                .build();

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUsuarioController_WhenCalledDeleteUsuario_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(usuarioService).deleteUsuario(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso: 1"));
    }

}
