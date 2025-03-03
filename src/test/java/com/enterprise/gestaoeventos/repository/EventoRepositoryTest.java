package com.enterprise.gestaoeventos.repository;

import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventoRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("SistemaEventosTest")
            .withUsername("postgres")
            .withPassword("dev1");

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario organizador;

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
        organizador = Usuario.builder()
                .nome("Organizador Teste")
                .email("organizador@email.com")
                .senha("senha123")
                .build();
        usuarioRepository.save(organizador);
    }

    @Test
    void testGivenEventoObject_WhenSaved_ShouldPersistEventoObject() {
        Evento evento = Evento.builder()
                .nome("Evento Teste")
                .tipoEvento(TipoEvento.TECNOLOGIA)
                .descricao("Um evento de teste")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Centro de Convenções")
                .capacidadeMaxima(100)
                .organizador(organizador)
                .build();

        Evento eventoSalvo = eventoRepository.save(evento);

        assertThat(eventoSalvo).isNotNull();
        assertThat(eventoSalvo.getId()).isNotNull();
    }

    @Test
    void testGivenEventoObject_WhenFindById_ShouldReturnEventoObject() {
        Evento evento = eventoRepository.save(Evento.builder()
                .nome("Evento Encontrado")
                .tipoEvento(TipoEvento.TECNOLOGIA)
                .descricao("Descrição")
                .dataInicio(LocalDateTime.now().plusDays(3))
                .dataFim(LocalDateTime.now().plusDays(4))
                .local("Auditório")
                .capacidadeMaxima(50)
                .organizador(organizador)
                .build());

        Optional<Evento> encontrado = eventoRepository.findById(evento.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Evento Encontrado");
    }
    @Test
    void testGivenEventoObject_WhenFindByTipoEvento_ShouldReturnEventoObject() {
        Evento evento = eventoRepository.save(Evento.builder()
                .nome("Evento Encontrado")
                .tipoEvento(TipoEvento.TECNOLOGIA)
                .descricao("Descrição")
                .dataInicio(LocalDateTime.now().plusDays(3))
                .dataFim(LocalDateTime.now().plusDays(4))
                .local("Auditório")
                .capacidadeMaxima(50)
                .organizador(organizador)
                .build());

        List<Evento> encontrado = eventoRepository.findByTipoEvento(evento.getTipoEvento());

        assertThat(encontrado).hasSize(1);
    }

    @Test
    void testGivenEventoObject_WhenFindAll_ShouldReturnListOfEventoObject() {
        eventoRepository.save(Evento.builder()
                .nome("Evento 1")
                .tipoEvento(TipoEvento.NEGOCIOS)
                .descricao("Descrição 1")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local 1")
                .capacidadeMaxima(30)
                .organizador(organizador)
                .build());

        eventoRepository.save(Evento.builder()
                .nome("Evento 2")
                .tipoEvento(TipoEvento.SAUDE)
                .descricao("Descrição 2")
                .dataInicio(LocalDateTime.now().plusDays(5))
                .dataFim(LocalDateTime.now().plusDays(6))
                .local("Local 2")
                .capacidadeMaxima(50)
                .organizador(organizador)
                .build());

        List<Evento> eventos = eventoRepository.findAll();

        assertThat(eventos).hasSize(2);
    }
    @Test
    void testGivenEventoObject_WhenFindDataInicioAfter_ShouldReturnListOfEventoObject() {
        eventoRepository.save(Evento.builder()
                .nome("Evento 1")
                .tipoEvento(TipoEvento.NEGOCIOS)
                .descricao("Descrição 1")
                .dataInicio(LocalDateTime.now().plusDays(1))
                .dataFim(LocalDateTime.now().plusDays(2))
                .local("Local 1")
                .capacidadeMaxima(30)
                .organizador(organizador)
                .build());

        eventoRepository.save(Evento.builder()
                .nome("Evento 2")
                .tipoEvento(TipoEvento.SAUDE)
                .descricao("Descrição 2")
                .dataInicio(LocalDateTime.now().plusDays(5))
                .dataFim(LocalDateTime.now().plusDays(6))
                .local("Local 2")
                .capacidadeMaxima(50)
                .organizador(organizador)
                .build());

        List<Evento> eventos = eventoRepository.findByDataInicioAfter(LocalDateTime.now());

        assertThat(eventos).hasSize(2);
    }

    @Test
    void testGivenEventoObject_WhenDelete_ShouldReturnEmpty() {
        Evento evento = eventoRepository.save(Evento.builder()
                .nome("Evento Deletável")
                .tipoEvento(TipoEvento.NEGOCIOS)
                .descricao("Será deletado")
                .dataInicio(LocalDateTime.now().plusDays(7))
                .dataFim(LocalDateTime.now().plusDays(8))
                .local("Local Deletável")
                .capacidadeMaxima(200)
                .organizador(organizador)
                .build());

        eventoRepository.delete(evento);

        Optional<Evento> eventoDeletado = eventoRepository.findById(evento.getId());

        assertThat(eventoDeletado).isEmpty();
    }
}

