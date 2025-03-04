package com.enterprise.gestaoeventos.model.dto;

import com.enterprise.gestaoeventos.model.enuns.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUsuarioDTO(

        @NotNull @Email(message = "E-mail inválido")
        String email,

        @NotNull @Size(min = 3, max = 30, message = "O nome deve ter ao menos 3 caracteres")
        String nome,

        @NotNull
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo pelo menos uma letra maiúscula, " +
                        "uma minúscula, um número e um caractere especial.")
        String senha,

        @NotNull
        Role role
) {}
