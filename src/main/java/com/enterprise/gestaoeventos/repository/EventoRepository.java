package com.enterprise.gestaoeventos.repository;

import com.enterprise.gestaoeventos.model.entity.Evento;
import com.enterprise.gestaoeventos.model.enuns.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByTipoEvento(TipoEvento tipoEvento);
    List<Evento> findByDataInicioAfter(LocalDateTime data);
}
