package com.autobots.automanager.controles;

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
public class ControleMercadoria {

    // so gerente cadastrar mercadoria
    @PreAuthorize("hasAnyRole('GERENTE')")
    @PostMapping("/cadastrar-mercadoria")
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody String mercadoria) {
        //implementacao do cadastro de mercadoria
        return new ResponseEntity<>("Mercadoria cadastrada: " + mercadoria, HttpStatus.CREATED);
    }

    // gerente e vendedor obter mercadoria
    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping("/obter-mercadorias")
    public ResponseEntity<String> obterMercadorias() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nomeUsuario = authentication.getName();
        return new ResponseEntity<>("Lista de mercadorias para usuário: " + nomeUsuario, HttpStatus.OK);
    }
}