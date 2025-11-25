package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.RepositorioCliente;

@RestController
public class ControleCliente {
	@Autowired
	private RepositorioCliente repositorio;
	//cadastrar cliente so pra admin
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/cadastrar-cliente")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	// get cliente todas as roles menos cliente
	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/obter-clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		return new ResponseEntity<>(repositorio.findAll(),HttpStatus.FOUND);
	}

	// todas as roles get by id menos cliente
	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/obter-cliente/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
		Optional<Cliente> cliente = repositorio.findById(id);
		if (cliente.isPresent()) {
			return new ResponseEntity<Cliente>(cliente.get(), HttpStatus.FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	//  atualizar cliente todas roles menos cliente 
	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@PutMapping("/atualizar-cliente/{id}")
	public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
		Optional<Cliente> clienteExistente = repositorio.findById(id);
		if (clienteExistente.isPresent()) {
			Cliente cliente = clienteExistente.get();
			cliente.setNome(clienteAtualizado.getNome());
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// excluir todas roles menos cliente
	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@DeleteMapping("/excluir-cliente/{id}")
	public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// ler as proprias infos so cliente
	@PreAuthorize("hasAnyRole('CLIENTE')")
	@GetMapping("/meu-cadastro")
	public ResponseEntity<String> meuCadastro() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nomeUsuario = authentication.getName();
		return new ResponseEntity<>("Informações do usuário: " + nomeUsuario, HttpStatus.OK);
	}
}