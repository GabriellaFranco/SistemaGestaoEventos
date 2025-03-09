package com.enterprise.gestaoeventos.model.entity;


import com.enterprise.gestaoeventos.model.enuns.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    @NotNull
    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<AtribuirPermissao> permissoes = new ArrayList<>();

    @OneToMany(mappedBy = "organizador", cascade = CascadeType.ALL)
    private List<Evento> eventosOrganizados;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos = new ArrayList<>();

}
