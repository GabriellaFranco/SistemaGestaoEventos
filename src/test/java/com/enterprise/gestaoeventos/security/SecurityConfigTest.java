package com.enterprise.gestaoeventos.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSecurityConfig_WhenNotAuthenticated_ShouldReturn401Status() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSecurityConfig_WhenAuthenticatedWithRoleADMIN_ShouldReturnListOfUsuarioObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "participante", roles = {"PARTICIPANTE"})
    void testSecurityConfig_WhenAuthenticatedWithRolePARTICIPANTE_ShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "organizador", roles = {"ORGANIZADOR"})
    void testSecurityConfig_WhenAuthenticatedWithRoleORGANIZADOR_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/inscricoes/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "participante", roles = {"PARTICIPANTE"})
    void testSecurityConfig_WhenAuthenticatedWithRolePARTICIPANTE_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/inscricoes/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(username = "participante", roles = {"PARTICIPANTE"})
    void testSecurityConfig_WhenAuthenticatedWithRolePARTICIPANTE_ShouldReturnIsNoContent() throws Exception {
        mockMvc.perform(get("/pagamentos"))
                .andExpect(status().isNoContent());
    }



}
