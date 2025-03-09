package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.service.InscricaoService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
public class InscricaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InscricaoService inscricaoService;

    private GetInscricaoDTO inscricao1;
    private GetInscricaoDTO inscricao2;

    @BeforeEach
    void setUp() {
        inscricao1 = GetInscricaoDTO.builder()
                .id(1L)
                .dataInscricao(LocalDateTime.now())
                .statusInscricao(StatusInscricao.PENDENTE)
                .evento(GetInscricaoDTO.EventoDTO.builder()
                        .nome("Teste")
                        .dataInicio(LocalDateTime.now().plusDays(1))
                        .dataFim(LocalDateTime.now().plusDays(4))
                        .build())
                .usuario(GetInscricaoDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Liana")
                        .build())
                .build();

        inscricao2 = GetInscricaoDTO.builder()
                .id(2L)
                .dataInscricao(LocalDateTime.now())
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .evento(GetInscricaoDTO.EventoDTO.builder()
                        .nome("Teste")
                        .dataInicio(LocalDateTime.now().plusDays(1))
                        .dataFim(LocalDateTime.now().plusDays(4))
                        .build())
                .usuario(GetInscricaoDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Gabriella")
                        .build())
                .build();
    }

    @Test
    void testInscricaoController_WhenCalledGetAllInscricoes_ShouldReturnListOfInscricoes() throws Exception {
        when(inscricaoService.getAllInscricoes()).thenReturn(List.of(inscricao1, inscricao2));

        mockMvc.perform(MockMvcRequestBuilders.get("/inscricoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].usuario.nome").value("Liana"));

    }

    @Test
    void testInscricaoController_WhenCalledGetAllInscricoesWithNoInscricoes_ShouldReturnEmptyList() throws Exception {
        when(inscricaoService.getAllInscricoes()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/inscricoes"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testInscricaoController_WhenCalledGetInscricaoById_ShouldReturnInscricaoObject() throws Exception {
        when(inscricaoService.getInscricaoById(1L)).thenReturn(inscricao1);

        mockMvc.perform(MockMvcRequestBuilders.get("/inscricoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.nome").value("Liana"));
    }

    @Test
    void testInscricaoController_WhenCalledGetInscricaoByIdWithInexistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(inscricaoService.getInscricaoById(5L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/inscricoes/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testInscricaoController_WhenCalledCreateInscricao_ShouldPersistAndReturnInscricaoObject() throws Exception {

        var createDto = CreateInscricaoDTO.builder()
                .usuarioId(1L)
                .eventoId(1L)
                .build();

        var inscricaoCriada = GetInscricaoDTO.builder()
                .dataInscricao(LocalDateTime.now())
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .build();

        when(inscricaoService.createInscricao(any(), anyString())).thenReturn(inscricaoCriada);

        mockMvc.perform(post("/inscricoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.nome").value("Gabriella"));
    }

    @Test
    void testInscricaoController_WhenCalledCreateInscricaoWithoutStatusPagamento_ShouldReturnBadRequest() throws Exception {
        var createDto = CreateInscricaoDTO.builder()
                .usuarioId(1L)
                .eventoId(1L)
                .build();

        var inscricaoCriado = inscricaoService.createInscricao(createDto, anyString());

        mockMvc.perform(post("/inscricoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInscricaoController_WhenCalledDeleteInscricao_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(inscricaoService).deleteInscricao(1L);

        mockMvc.perform(delete("/inscricoes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Inscricao deletada com sucesso: 1"));
    }
}
