package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
public class ControleUsuario {

	@Autowired
	private RepositorioUsuario repositorio;

	@PostMapping("/cadastrar-usuario")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		try {
			Credencial credencial = new Credencial();
			credencial.setNomeUsuario(usuario.getCredencial().getNomeUsuario());
			String senha = codificador.encode(usuario.getCredencial().getSenha());
			credencial.setSenha(senha);
			usuario.setCredencial(credencial);
			repositorio.save(usuario);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/obter-usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.FOUND);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/obter-usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isPresent()) {
			return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/atualizar-usuario/{id}")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
		Optional<Usuario> usuarioExistente = repositorio.findById(id);
		if (usuarioExistente.isPresent()) {
			Usuario usuario = usuarioExistente.get();
			usuario.setNome(usuarioAtualizado.getNome());
			usuario.setPerfis(usuarioAtualizado.getPerfis());
			
			//se a senha foi alterada, criptografar
			if (usuarioAtualizado.getCredencial() != null && usuarioAtualizado.getCredencial().getSenha() != null) {
				BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
				usuario.getCredencial().setSenha(codificador.encode(usuarioAtualizado.getCredencial().getSenha()));
			}
			
			repositorio.save(usuario);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/excluir-usuario/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}