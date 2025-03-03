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
public class InscricaoRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("SistemaEventosTest")
            .withUsername("postgres")
            .withPassword("dev1");

    @Autowired
    private InscricaoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

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
                .role(Role.PARTICIPANTE)
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

        inscricao = repository.save(Inscricao.builder()
                .usuario(usuario)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .build());

        pagamento = pagamentoRepository.save(Pagamento.builder()
                .inscricao(inscricao)
                .dataPagamento(LocalDateTime.now())
                .valor(new BigDecimal(30))
                .status(StatusPagamento.APROVADO)
                .build());
    }

    @Test
    void testGivenInscricaoObject_WhenSaved_ShouldPersistInscricaoObject() {
        Inscricao inscricao = Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build();

        Inscricao inscricaoSalva = repository.save(inscricao);

        assertThat(inscricaoSalva).isNotNull();
        assertThat(inscricaoSalva.getId()).isNotNull();
        assertThat(inscricaoSalva.getUsuario().getNome()).isEqualTo("Liana");
    }

    @Test
    void testGivenInscricaoObject_WhenSavedWithoutEvento_ShouldThrowException() {
        Inscricao inscricao = Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .pagamento(pagamento)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(inscricao));
        assertThat(inscricao.getId()).isNull();
    }

    @Test
    void testGivenInscricaoObject_WhenFindById_ShouldReturnInscricaoObject() {
        Inscricao inscricao = Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build();

        repository.save(inscricao);

        Optional<Inscricao> inscricaoEncontrada = repository.findById(inscricao.getId());

        assertThat(inscricaoEncontrada).isPresent();
        assertThat(inscricaoEncontrada.get().getId()).isEqualTo(inscricao.getId());
    }

    @Test
    void testGivenInscricaoId_WhenFindByUsuarioId_ShouldReturnListOfInscricaoObject() {
        Inscricao inscricao = repository.save(Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build());

        Inscricao inscricao2 = repository.save(Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.PENDENTE)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build());

        List<Inscricao> inscricaoEncontrada = repository.findByUsuarioId(inscricao.getUsuario().getId());

        assertEquals(3,inscricaoEncontrada.size());
    }
    @Test
    void testGivenInscricaoId_WhenFindAll_ShouldReturnListOfInscricaoObject() {
        Inscricao inscricao = repository.save(Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build());

        Inscricao inscricao2 = repository.save(Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.PENDENTE)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build());

        List<Inscricao> inscricaoEncontrada = repository.findAll();

        assertEquals(3,inscricaoEncontrada.size());
    }

    @Test
    void testGivenInscricaoObject_WhenDelete_ShouldReturnEmpty() {
        Inscricao inscricao = repository.save(Inscricao.builder()
                .usuario(usuario)
                .statusInscricao(StatusInscricao.CONFIRMADA)
                .dataInscricao(LocalDateTime.now())
                .evento(evento)
                .pagamento(pagamento)
                .build());

        repository.delete(inscricao);
        Optional<Inscricao> inscricaoDeletada = repository.findById(inscricao.getId());

        assertThat(inscricaoDeletada).isEmpty();
    }


}
