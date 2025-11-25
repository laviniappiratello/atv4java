package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControleServico {

    @PreAuthorize("hasAnyRole('GERENTE')")
    @PostMapping("/cadastrar-servico")
    public ResponseEntity<?> cadastrarServico(@RequestBody String servico) {
        // Implementação do cadastro de serviço
        return new ResponseEntity<>("Serviço cadastrado: " + servico, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping("/obter-servicos")
    public ResponseEntity<String> obterServicos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nomeUsuario = authentication.getName();
        return new ResponseEntity<>("Lista de serviços para usuário: " + nomeUsuario, HttpStatus.OK);
    }
}