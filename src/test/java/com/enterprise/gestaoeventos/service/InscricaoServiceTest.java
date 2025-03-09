package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreateInscricaoDTO;
import com.enterprise.gestaoeventos.model.dto.GetInscricaoDTO;
import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.entity.Inscricao;
import com.enterprise.gestaoeventos.model.entity.Pagamento;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.model.mapper.InscricaoMapper;
import com.enterprise.gestaoeventos.repository.InscricaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscricaoServiceTest {

    @Mock
    private InscricaoMapper mapper;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private InscricaoService inscricaoService;

    private Inscricao inscricao;
    private Evento evento;
    private Usuario usuario;
    private GetInscricaoDTO getInscricaoDTO;
    private CreateInscricaoDTO createInscricaoDTO;

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

        usuario = Usuario.builder()
                .id(1L)
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@30")
                .build();

        Pagamento pagamento = Pagamento.builder()
                .id(1L)
                .status(StatusPagamento.PENDENTE)
                .valor(new BigDecimal(40))
                .dataPagamento(LocalDateTime.now())
                .build();

        inscricao = Inscricao.builder()
                .id(1L)
                .usuario(usuario)
                .evento(evento)
                .pagamento(pagamento)
                .statusInscricao(StatusInscricao.PENDENTE)
                .dataInscricao(LocalDateTime.now())
                .build();

        createInscricaoDTO = CreateInscricaoDTO.builder()
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .build();

        getInscricaoDTO = GetInscricaoDTO.builder()
                .id(1L)
                .usuario(GetInscricaoDTO.UsuarioDTO.builder()
                        .id(1L)
                        .nome("Liana")
                        .build())
                .evento(GetInscricaoDTO.EventoDTO.builder()
                        .nome("Test")
                        .dataFim(LocalDateTime.now().plusDays(3))
                        .dataInicio(LocalDateTime.now().plusDays(1))
                        .build())
                .pagamento(GetInscricaoDTO.PagamentoDTO.builder()
                        .id(1L)
                        .statusPagamento(StatusPagamento.APROVADO)
                        .dataPagamento(LocalDateTime.now())
                        .valor(new BigDecimal(20))
                        .build())
                .statusInscricao(StatusInscricao.PENDENTE)
                .dataInscricao(LocalDateTime.now())
                .build();
    }

    @Test
    void testInscricaoService_WhenCalledGetAllInscricoes_ShouldReturnListOfInscricaoObject() {
        when(inscricaoRepository.findAll()).thenReturn(List.of(inscricao));
        when(mapper.toGetInscricaoDTO(inscricao)).thenReturn(getInscricaoDTO);

        List<GetInscricaoDTO> inscricoes = inscricaoService.getAllInscricoes();

        assertEquals(1, inscricoes.size());
    }

    @Test
    void testInscricaoService_WhenCalledFindInscricaoById_ShouldReturnInscricaoObject() {
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));
        when(mapper.toGetInscricaoDTO(inscricao)).thenReturn(getInscricaoDTO);

        GetInscricaoDTO inscricaoEncontrada = inscricaoService.getInscricaoById(1L);

        assertThat(inscricaoEncontrada).isNotNull();
        assertThat(inscricaoEncontrada.evento().nome()).isEqualTo("Test");
    }

    @Test
    void testInscricaoService_WhenCalledFindInscricaoByIdWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(inscricaoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscricaoService.getInscricaoById(2L));
    }

//    @Test
//    void testInscricaoService_WhenCalledCreateInscricao_ShouldPersistAndReturnInscricaoObject() {
//        when(mapper.toInscricao(createInscricaoDTO)).thenReturn(inscricao);
//        when(inscricaoRepository.save(inscricao)).thenReturn(inscricao);
//        when(mapper.toGetInscricaoDTO(inscricao)).thenReturn(getInscricaoDTO);
//
//        var inscricaoCriada = inscricaoService.createInscricao(createInscricaoDTO);
//
//        assertThat(inscricaoCriada).isNotNull();
//        assertThat(inscricaoCriada.evento().nome()).isEqualTo("Test");
//    }

    @Test
    void testInscricaoService_WhenCalledDeleteInscricaoById_ShouldReturnNothing() {
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));

        inscricaoService.deleteInscricao(1L);

        verify(inscricaoRepository, times(1)).delete(any());
    }

    @Test
    void testInscricaoService_WhenCalledDeleteInscricaoByIdWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(inscricaoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inscricaoService.deleteInscricao(2L));
    }

}
