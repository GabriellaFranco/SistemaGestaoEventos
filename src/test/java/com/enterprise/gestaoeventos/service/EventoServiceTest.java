package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateEventoDTO;
import com.enterprise.gestaoeventos.model.dto.GetEventoDTO;
import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.mapper.EventoMapper;
import com.enterprise.gestaoeventos.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {

    @Mock
    private EventoRepository repository;

    @Mock
    private EventoMapper mapper;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private GetEventoDTO getEventoDTO;
    private CreateEventoDTO createEventoDTO;

    @BeforeEach
    void setUp() {
        evento = Evento.builder()
                .id(1L)
                .nome("Evento Teste")
                .descricao("Descrição do evento")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local Teste")
                .capacidadeMaxima(50)
                .build();

        getEventoDTO = GetEventoDTO.builder()
                .id(1L)
                .nome("Evento Teste")
                .descricao("Descrição do evento")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local Teste")
                .capacidadeMaxima(50)
                .build();

        createEventoDTO = CreateEventoDTO.builder()
                .nome("Evento Teste")
                .descricao("Descrição do evento")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local Teste")
                .capacidadeMaxima(50)
                .build();
    }

    @Test
    void testEventoService_WhenCalledMethodFindAllEventos_ShouldReturnListOfEventoObject() {
        when(repository.findAll()).thenReturn(List.of(evento));
        when(mapper.toGetEventoDTO(evento)).thenReturn(getEventoDTO);

        List<GetEventoDTO> eventos = eventoService.getAllEventos();

        assertEquals(1, eventos.size());
        assertEquals("Evento Teste", eventos.get(0).nome());
    }

    @Test
    void testEventoService_WhenCalledMethodGetEventoById_ShouldReturnEventoObject() {
        when(repository.findById(1L)).thenReturn(Optional.of(evento));
        when(mapper.toGetEventoDTO(evento)).thenReturn(getEventoDTO);

        GetEventoDTO eventoEncontrado = eventoService.getEventoById(1L);

        assertThat(eventoEncontrado).isNotNull();
        assertEquals("Evento Teste", eventoEncontrado.nome());
        assertThat(eventoEncontrado.id()).isEqualTo(evento.getId());
    }

    @Test
    void testEventoService_WhenCalledMethodGetEventoByIdWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventoService.getEventoById(2L));
    }

//    @Test
//    void testEventoService_WhenCalledMethodCreateEvento_ShouldPersistAndReturnEventoObject() {
//        when(mapper.toEvento(createEventoDTO)).thenReturn(evento);
//        when(repository.save(evento)).thenReturn(evento);
//        when(mapper.toGetEventoDTO(evento)).thenReturn(getEventoDTO);
//
//        GetEventoDTO eventoCriado = eventoService.createEvento(createEventoDTO);
//
//        assertThat(eventoCriado).isNotNull();
//        assertEquals("Evento Teste", eventoCriado.nome());
//    }

    @Test
    void testEventoService_WhenCalledMethodDeleteEvento_ShouldReturnNothing() {
        when(repository.findById(1L)).thenReturn(Optional.of(evento));

        eventoService.deleteEvento(1L);

        verify(repository, times(1)).delete(any());
    }

    @Test
    void testEventoService_WhenCalledMethodDeleteEventoWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventoService.deleteEvento(2L));
    }

}
