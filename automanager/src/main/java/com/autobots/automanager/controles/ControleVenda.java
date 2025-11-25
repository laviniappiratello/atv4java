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
public class ControleVenda {

    @PreAuthorize("hasAnyRole('GERENTE')")
    @PostMapping("/cadastrar-venda")
    public ResponseEntity<?> cadastrarVenda(@RequestBody String venda) {
        return new ResponseEntity<>("Venda cadastrada: " + venda, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('VENDEDOR')")
    @PostMapping("/criar-venda")
    public ResponseEntity<?> criarVenda(@RequestBody String venda) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String vendedor = authentication.getName();
        return new ResponseEntity<>("Venda criada pelo vendedor " + vendedor + ": " + venda, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'VENDEDOR')")
    @GetMapping("/obter-vendas")
    public ResponseEntity<String> obterVendas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nomeUsuario = authentication.getName();
        boolean isGerente = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_GERENTE"));
        
        if (isGerente) {
            return new ResponseEntity<>("Todas as vendas (acesso de gerente): " + nomeUsuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Suas vendas (acesso de vendedor): " + nomeUsuario, HttpStatus.OK);
        }
    }
}