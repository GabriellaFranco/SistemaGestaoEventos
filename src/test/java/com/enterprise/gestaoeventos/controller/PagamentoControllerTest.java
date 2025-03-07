package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.service.PagamentoService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagamentoService pagamentoService;

    private GetPagamentoDTO pagamento1;
    private GetPagamentoDTO pagamento2;

    @BeforeEach
    void setup() {

        pagamento1 = GetPagamentoDTO.builder()
                .id(1L)
                .dataPagamento(LocalDateTime.now())
                .statusPagamento(StatusPagamento.APROVADO)
                .inscricao(GetPagamentoDTO.InscricaoDTO.builder()
                        .id(1L)
                        .statusInscricao(StatusInscricao.CONFIRMADA)
                        .build())
                .valor(new BigDecimal(50))
                .build();

        pagamento2 = GetPagamentoDTO.builder()
                .id(1L)
                .dataPagamento(LocalDateTime.now())
                .statusPagamento(StatusPagamento.PENDENTE)
                .inscricao(GetPagamentoDTO.InscricaoDTO.builder()
                        .id(1L)
                        .statusInscricao(StatusInscricao.PENDENTE)
                        .build())
                .valor(new BigDecimal(10))
                .build();

    }

    @Test
    void testPagamentoController_WhenCalledGetAllPagamentos_ShouldReturnListOfPagamentoObject() throws Exception {
        when(pagamentoService.getAllPagamentos()).thenReturn(List.of(pagamento1, pagamento2));

        mockMvc.perform(get("/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
    @Test
    void testPagamentoController_WhenCalledGetAllPagamentosWithNoPagamentos_ShouldReturnEmptyList() throws Exception {
        when(pagamentoService.getAllPagamentos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pagamentos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPagamentoController_WhenCalledGetPagamentoById_ShouldReturnPagamentoObject() throws Exception {
        when(pagamentoService.getPagamentoById(1L)).thenReturn(pagamento1);

        mockMvc.perform(MockMvcRequestBuilders.get("/pagamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(50));
    }

    @Test
    void testPagamentoController_WhenCalledGetPagamentoByIdWithInexistentId_ShouldThrowResourceNotFoundException() throws Exception {
        when(pagamentoService.getPagamentoById(5L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/pagamentos/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPagamentoController_WhenCalledCreatePagamento_ShouldPersistAndReturnPagamentoObject() throws Exception {
        var createDto = CreatePagamentoDTO.builder()
                .dataPagamento(LocalDateTime.now().plusMinutes(1))
                .statusPagamento(StatusPagamento.PENDENTE)
                .valor(new BigDecimal(30))
                .build();

        var pagamentoCriado = GetPagamentoDTO.builder()
                .id(3L)
                .dataPagamento(LocalDateTime.now())
                .statusPagamento(StatusPagamento.PENDENTE)
                .inscricao(GetPagamentoDTO.InscricaoDTO.builder()
                        .id(1L)
                        .statusInscricao(StatusInscricao.PENDENTE)
                        .build())
                .valor(new BigDecimal(10))
                .build();

        when(pagamentoService.createPagamento(createDto)).thenReturn(pagamentoCriado);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor").value(10));

    }
    @Test
    void testPagamentoController_WhenCalledCreatePagamentoWithoutValor_ShouldReturnBadRequest() throws Exception {
        var createDto = CreatePagamentoDTO.builder()
                .dataPagamento(LocalDateTime.now().plusMinutes(1))
                .statusPagamento(StatusPagamento.PENDENTE)
                .build();

        pagamentoService.createPagamento(createDto);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testPagamentoController_WhenCalledDeletePagamento_ReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(pagamentoService).deletePagamento(1L);

        mockMvc.perform(delete("/pagamentos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Pagamento deletado com sucesso: 1"));
    }
}
