package com.enterprise.gestaoeventos.repository;

import com.enterprise.gestaoeventos.model.entity.Usuario;
import com.enterprise.gestaoeventos.model.enuns.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("SistemaEventosTest")
            .withUsername("postgres")
            .withPassword("dev1");

    @Autowired
    private UsuarioRepository repository;

    @BeforeAll
    static void containerStart() {
        postgres.start();
    }

    @AfterAll
    static void containerClose() {
        postgres.close();
    }

    @Test
    void testGivenUsuarioObject_WhenSaved_ShouldPersistUsuarioObject() {
        Usuario usuario = repository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        assertThat(usuario).isNotNull();
        assertThat(usuario.getId()).isNotNull();
    }

    @Test
    void testGivenUsuarioObject_WhenSavedWithoutNome_ShouldThrowException() {
        Usuario usuario = Usuario.builder()
                .email("liana@gmail.com")
                .senha("liana2077")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(usuario));
        assertThat(usuario.getId()).isNull();
    }

    @Test
    void testGivenUsuarioObject_WhenDelete_ShouldReturnEmpty() {
        Usuario usuario = repository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        repository.delete(usuario);
        Optional<Usuario> usuarioDeletado = repository.findById(usuario.getId());

        assertThat(usuarioDeletado).isEmpty();
    }

    @Test
    void testGivenUsuarioObject_WhenFindById_ShouldReturnUsuarioObject() {
        Usuario usuario = repository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        Optional<Usuario> usuarioEncontrado = repository.findById(usuario.getId());

        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getId()).isEqualTo(usuario.getId());
    }

    @Test
    void givenUsuarioObject_WhenFindByEmail_ShouldReturnUsuarioObject() {
        Usuario usuario = repository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        Optional<Usuario> usuarioEncontrado = repository.findByEmail(usuario.getEmail());

        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getEmail()).isEqualTo(usuario.getEmail());
    }

    @Test
    void givenUsuarioObject_WhenFindAll_ShouldReturnListOfUsuarioObject() {
        Usuario usuario = repository.save(Usuario.builder()
                .nome("Liana")
                .email("liana@gmail.com")
                .senha("liana2077")
                .build());

        Usuario usuario2 = repository.save(Usuario.builder()
                .nome("Gabriella")
                .email("gabriella@gmail.com")
                .senha("gabi2077")
                .build());

        List<Usuario> usuarios = repository.findAll();

        assertThat(usuarios).isNotEmpty();
        assertThat(usuarios).hasSize(2);
    }

}
