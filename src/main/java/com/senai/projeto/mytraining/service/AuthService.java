package com.senai.projeto.mytraining.service;

import com.senai.projeto.mytraining.dto.request.LoginRequestDTO;
import com.senai.projeto.mytraining.dto.request.UsuarioRequestDTO;
import com.senai.projeto.mytraining.dto.response.LoginResponseDTO;
import com.senai.projeto.mytraining.dto.response.UsuarioResponseDTO;
import com.senai.projeto.mytraining.mapper.UsuarioMapper;
import com.senai.projeto.mytraining.model.Role;
import com.senai.projeto.mytraining.model.Usuario;
import com.senai.projeto.mytraining.repository.RoleRepository;
import com.senai.projeto.mytraining.repository.UsuarioRepository;
import com.senai.projeto.mytraining.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {


    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public Optional<LoginResponseDTO> login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(dto.email());
            String token = jwtUtil.generateToken(userDetails);

            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(dto.email());
            if (usuarioOptional.isEmpty()) {
                return Optional.empty();
            }

            UsuarioResponseDTO usuarioDTO = usuarioMapper.toResponseDTO(usuarioOptional.get());
            return Optional.of(new LoginResponseDTO(token, usuarioDTO));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public UsuarioResponseDTO registro(UsuarioRequestDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        // Atribuir ROLE_USER automaticamente para novos usuários
        Optional<Role> roleUser = roleRepository.findByNome("ROLE_USER");
        if (roleUser.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleUser.get());
            usuario.setRoles(roles);
        }

        // Se rolesIds foram fornecidos no DTO, adicionar também
        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            Set<Role> rolesAdicionais = new HashSet<>(roleRepository.findAllById(dto.rolesIds()));
            usuario.getRoles().addAll(rolesAdicionais);
        }

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }
}

