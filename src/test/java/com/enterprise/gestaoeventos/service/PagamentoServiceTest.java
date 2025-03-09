package com.enterprise.gestaoeventos.service;

import com.enterprise.gestaoeventos.exception.ResourceNotFoundException;
import com.enterprise.gestaoeventos.model.dto.CreatePagamentoDTO;
import com.enterprise.gestaoeventos.model.dto.GetPagamentoDTO;
import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.entity.Inscricao;
import com.enterprise.gestaoeventos.model.entity.Pagamento;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.model.mapper.PagamentoMapper;
import com.enterprise.gestaoeventos.repository.PagamentoRepository;
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
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PagamentoMapper mapper;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;
    private Inscricao inscricao;
    private GetPagamentoDTO getPagamentoDTO;
    private CreatePagamentoDTO createPagamentoDTO;

    @BeforeEach
    void setup() {

        Evento evento = Evento.builder()
                .id(1L)
                .nome("Evento Teste")
                .descricao("Descrição do evento")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local Teste")
                .capacidadeMaxima(50)
                .build();

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("Liana@30")
                .build();

        Pagamento pagamento1 = Pagamento.builder()
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

        pagamento = Pagamento.builder()
                .id(1L)
                .inscricao(inscricao)
                .dataPagamento(LocalDateTime.now())
                .status(StatusPagamento.PENDENTE)
                .valor(new BigDecimal(20))
                .build();

        getPagamentoDTO = GetPagamentoDTO.builder()
                .id(1L)
                .inscricao(GetPagamentoDTO.InscricaoDTO.builder()
                        .statusInscricao(StatusInscricao.PENDENTE)
                        .id(2L)
                        .build())
                .dataPagamento(LocalDateTime.now())
                .valor(new BigDecimal(20))
                .statusPagamento(StatusPagamento.PENDENTE)
                .build();

        createPagamentoDTO = CreatePagamentoDTO.builder()
                .dataPagamento(LocalDateTime.now())
                .statusPagamento(StatusPagamento.PENDENTE)
                .valor(new BigDecimal(70))
                .build();
    }

    @Test
    void testPagamentoService_WhenCalledGetAllPagamentos_ShouldReturnListOfPagamentoObject() {
        when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));
        when(mapper.toGetPagamentoDTO(pagamento)).thenReturn(getPagamentoDTO);

        List<GetPagamentoDTO> pagamentos = pagamentoService.getAllPagamentos();

        assertThat(pagamentos).isNotNull();
        assertEquals(1, pagamentos.size());
    }

    @Test
    void testPagamentoService_WhenCalledGetPagamentoById_ShouldReturnPagamentoObject() {
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(mapper.toGetPagamentoDTO(pagamento)).thenReturn(getPagamentoDTO);

        var pagamentoEncontrado = pagamentoService.getPagamentoById(1L);

        assertThat(pagamentoEncontrado).isNotNull();
        assertThat(pagamentoEncontrado.statusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void testPagamentoService_WhenCalledGetPagamentoByIdWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(pagamentoRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> pagamentoService.getPagamentoById(5L));
    }

//    @Test
//    void testPagamentoService_WhenCalledCreatePagamento_ShouldPersistAndReturnPagamentoObject() {
//        when(mapper.toPagamento(createPagamentoDTO)).thenReturn(pagamento);
//        when(pagamentoRepository.save(pagamento)).thenReturn(pagamento);
//        when(mapper.toGetPagamentoDTO(pagamento)).thenReturn(getPagamentoDTO);
//
//        var pagamentoCriado = pagamentoService.createPagamento(createPagamentoDTO);
//
//        assertThat(pagamentoCriado).isNotNull();
//        assertThat(pagamentoCriado.statusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
//    }

    @Test
    void testPagamentoService_WhenCalledDeletePagamento_ShouldReturnNothing() {
        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamento));

        pagamentoService.deletePagamento(1L);

        verify(pagamentoRepository, times(1)).delete(pagamento);
    }

    @Test
    void testPagamentoService_WhenCalledDeletePagamentoWithInexistentId_ShouldThrowResourceNotFoundException() {
        when(pagamentoRepository.findById(5L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> pagamentoService.deletePagamento(5L));
    }
}
