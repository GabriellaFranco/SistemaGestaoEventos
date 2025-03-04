package com.enterprise.gestaoeventos.model.mapper;

import com.enterprise.gestaoeventos.model.dto.CreateUsuarioDTO;
import com.enterprise.gestaoeventos.model.dto.GetUsuarioDTO;
import com.enterprise.gestaoeventos.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toUsuario(CreateUsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .email(usuarioDTO.email())
                .nome(usuarioDTO.nome())
                .senha(usuarioDTO.senha())
                .role(usuarioDTO.role())
                .build();
    }

    public GetUsuarioDTO toGetUsuarioDTO(Usuario usuario) {
        return GetUsuarioDTO.builder()
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .senha(usuario.getSenha())
                .role(usuario.getRole())
                .eventosOrganizados(usuario.getEventosOrganizados().stream().map(evento -> GetUsuarioDTO.EventoDTO.builder()
                        .id(evento.getId())
                        .nome(evento.getNome())
                        .dataInicio(evento.getDataInicio())
                        .dataFim(evento.getDataFim())
                        .build()).toList())
                .inscricoes(usuario.getInscricoes().stream().map(inscricao -> GetUsuarioDTO.InscricaoDTO.builder()
                        .id(inscricao.getId())
                        .statusInscricao(inscricao.getStatusInscricao())
                        .build()).toList())
                .build();
    }
}
