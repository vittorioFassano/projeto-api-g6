package org.serratec.backend.projetoapig6.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.serratec.backend.projetoapig6.dto.UsuarioDTO;
import org.serratec.backend.projetoapig6.dto.UsuarioInserirDTO;
import org.serratec.backend.projetoapig6.exception.EmailException;
import org.serratec.backend.projetoapig6.exception.NotFoundException;
import org.serratec.backend.projetoapig6.exception.SenhaException;
import org.serratec.backend.projetoapig6.model.Usuario;
import org.serratec.backend.projetoapig6.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public List<UsuarioDTO> findAll() {
		List<Usuario> usuarios = usuarioRepository.findAll();

		List<UsuarioDTO> usuarioDTO = new ArrayList<>();

		for (Usuario usuario : usuarios) {
			UsuarioDTO usuDTO = new UsuarioDTO(usuario);
			usuarioDTO.add(usuDTO);
		}

		// List<UsuarioDTO> usuarioDTO =
		// usuarios.stream().map(UsuarioDTO::new).toList();

		return usuarioDTO;
	}

	public Usuario findById(Long id) throws NotFoundException {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
		if (usuarioOpt.isEmpty()) {
			throw new NotFoundException();
		}
		return usuarioOpt.get();
	}

	@Transactional
	public UsuarioDTO inserir(UsuarioInserirDTO usuarioInserirDTO) throws EmailException, SenhaException {
		if (!usuarioInserirDTO.getSenha().equalsIgnoreCase(usuarioInserirDTO.getConfirmaSenha())) {
			throw new SenhaException("Senha e Confirma Senha não são iguais");
		}
		Usuario usuarioBd = usuarioRepository.findByEmail(usuarioInserirDTO.getEmail());
		if (usuarioBd != null) {
			throw new EmailException("Email ja existente");
		}

		Usuario usuario = new Usuario();
		usuario.setNome(usuarioInserirDTO.getNome());
		usuario.setEmail(usuarioInserirDTO.getEmail());
		usuario.setSenha(encoder.encode(usuarioInserirDTO.getSenha()));

		usuario = usuarioRepository.save(usuario);

		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
		return usuarioDTO;
	}

}
