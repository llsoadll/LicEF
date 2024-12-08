package com.licenciatura.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.licenciatura.model.Usuario;
import com.licenciatura.repository.UsuarioRepository;

@Service
public class InitService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);

            Usuario invitado = new Usuario();
            invitado.setUsername("invitado");
            invitado.setPassword(passwordEncoder.encode("invitado123"));
            invitado.setRol("INVITADO");
            usuarioRepository.save(invitado);
        }
    }
}
