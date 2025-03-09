package com.enterprise.gestaoeventos.controller;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateEventoDTO;
import com.enterprise.gestaoeventos.model.dto.GetEventoDTO;
import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import com.enterprise.gestaoeventos.service.EventoService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
public class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventoService eventoService;

    private GetEventoDTO evento1;
    private GetEventoDTO evento2;

    @BeforeEach
    void setup() {
        evento1 = GetEventoDTO.builder()
                .id(1L)
                .nome("Evento 1")
                .descricao("Desc 1")
                .local("Local 1")
                .dataInicio(LocalDateTime.now().plusDays(3))
                .dataFim(LocalDateTime.now().plusDays(5))
                .tipoEvento(TipoEvento.TECNOLOGIA)
                .build();

        evento2 = GetEventoDTO.builder()
                .id(2L)
                .nome("Evento 2")
                .descricao("Desc 2")
                .local("Local 2")
                .dataInicio(LocalDateTime.now().plusDays(5))
                .dataFim(LocalDateTime.now().plusDays(8))
                .tipoEvento(TipoEvento.ARTES)
                .build();

    }

    @Test
    void testEventoController_WhenCalledGetAllEventos_ShouldReturnListOfEventoObject() throws Exception {
        when(eventoService.getAllEventos()).thenReturn(List.of(evento1, evento2));

        mockMvc.perform(MockMvcRequestBuilders.get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Evento 1"));
    }

    @Test
    void testEventoController_WhenCalledGetAllEventosWithNoExistingEventos_ShouldReturnEmptyList() throws Exception {
        when(eventoService.getAllEventos()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/eventos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEventoController_WhenCalledGetEventoById_ShouldReturnEventoObject() throws Exception {
        when(eventoService.getEventoById(1L)).thenReturn(evento1);

        mockMvc.perform(MockMvcRequestBuilders.get("/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Evento 1"));

    }

    @Test
    void testEventoController_WhenCalledGetEventoByIdWithInexistentId_ShouldThrowResourceNotFoundExcecption() throws Exception {
        when(eventoService.getEventoById(5L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/eventos/5"))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void testEventoController_WhenCalledCreateEvento_ShouldSaveAndReturnEventoObject() throws Exception {
//
//        var createDTO = CreateEventoDTO.builder()
//                .nome("Novo Evento")
//                .descricao("Descrição do evento que acontecerá")
//                .local("Local do evento")
//                .dataInicio(LocalDateTime.now().plusDays(10))
//                .dataFim(LocalDateTime.now().plusDays(11))
//                .tipoEvento(TipoEvento.ARTES)
//                .capacidadeMaxima(800)
//                .build();
//
//        var eventoCriado = GetEventoDTO.builder()
//                .nome("Novo Evento")
//                .descricao("Descrição do evento que acontecerá")
//                .local("Local do evento")
//                .dataInicio(LocalDateTime.now().plusDays(3))
//                .dataFim(LocalDateTime.now().plusDays(5))
//                .tipoEvento(TipoEvento.TECNOLOGIA)
//                .capacidadeMaxima(800)
//                .build();
//
//        when(eventoService.createEvento(any())).thenReturn(eventoCriado);
//
//        mockMvc.perform(post("/eventos")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.nome").value("Novo Evento"));
//    }

    @Test
    void testEventoController_WhenCalledCreateEventoWithoutDataInicio_ShouldReturnBadRequest() throws Exception {

        var createDTO = CreateEventoDTO.builder()
                .nome("Novo Evento")
                .descricao("Descrição do evento que acontecerá")
                .local("Local do evento")
                .dataFim(LocalDateTime.now().plusDays(5))
                .tipoEvento(TipoEvento.TECNOLOGIA)
                .capacidadeMaxima(800)
                .build();

        mockMvc.perform(post("/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEventoController_WhenCalledDeleteEvento_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(eventoService).deleteEvento(1L);

        mockMvc.perform(delete("/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento deletado com sucesso: 1"));
    }

}
