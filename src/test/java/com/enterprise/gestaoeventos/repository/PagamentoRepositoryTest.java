package com.enterprise.gestaoeventos.repository;

import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.entity.Inscricao;
import com.enterprise.gestaoeventos.model.entity.Pagamento;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.Role;
import com.enterprise.gestaoeventos.model.enuns.StatusInscricao;
import com.enterprise.gestaoeventos.model.enuns.StatusPagamento;
import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PagamentoRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("SistemaEventosTest")
            .withUsername("postgres")
            .withPassword("dev1");

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    private Usuario usuario;
    private Evento evento;
    private Pagamento pagamento;
    private Inscricao inscricao;

    @BeforeAll
    static void containerStart() {
        postgres.start();
    }

    @AfterAll
    static void containerClose() {
        postgres.close();
    }

    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        evento = eventoRepository.save(Evento.builder()
                .nome("Evento 1")
                .tipoEvento(TipoEvento.NEGOCIOS)
                .descricao("Descrição 1")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local 1")
                .capacidadeMaxima(30)
                .organizador(usuario)
                .build());

        inscricao = inscricaoRepository.save(Inscricao.builder()
                .usuario(usuario)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .build());

        pagamento = repository.save(Pagamento.builder()
                .inscricao(inscricao)
                .dataPagamento(LocalDateTime.now())
                .valor(new BigDecimal(30))
                .status(StatusPagamento.APROVADO)
                .build());
    }

    @Test
    void testGivenPagamentoObject_WhenSaved_ShouldPersistPagamento() {
        assertThat(pagamento).isNotNull();
        assertThat(pagamento.getId()).isNotNull();
    }

    @Test
    void testGivenPagamentoObject_WhenSavedWithoutInscricao_ShouldThrowException() {
        Pagamento pagamento1 = Pagamento.builder()
                .dataPagamento(LocalDateTime.now())
                .valor(new BigDecimal(30))
                .status(StatusPagamento.APROVADO)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(pagamento1));
    }

    @Test
    void testGivenPagamentoObject_WhenFindById_ShouldReturnPagamentoObject() {
        Optional<Pagamento> pagamentoEncontrado = repository.findById(pagamento.getId());

        assertThat(pagamentoEncontrado).isPresent();
        assertThat(pagamentoEncontrado.get().getId()).isEqualTo(pagamento.getId());
    }

    @Test
    void testGivenPagamentoObject_WhenFindByStatus_ShouldReturnListOfPagamentoObject() {
        List<Pagamento> pagamentos = repository.findByStatus(pagamento.getStatus());

        assertThat(pagamentos).isNotNull();
        assertEquals(1, pagamentos.size());
    }

    @Test
    void testGivenPagamentoObject_WhenFindAll_ShouldReturnListOfPagamentoObject() {
        List<Pagamento> pagamentos = repository.findAll();

        assertThat(pagamentos).isNotNull();
        assertEquals(1, pagamentos.size());
    }

    @Test
    void testGivenPagamentoObject_WhenDelete_ShouldReturnEmpty() {
        repository.delete(pagamento);
        Optional<Pagamento> pagamentoDeletado = repository.findById(pagamento.getId());

        assertThat(pagamentoDeletado).isEmpty();
    }

}
