package wms.orion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wms.orion.dto.inboundDTO.CreateUserDTO;
import wms.orion.models.client.User;
import wms.orion.models.repositories.UserRepository;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Necessário para o post de amanhã!

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO dto) {

        // 1. Criar a entidade User
        User user = new User();
        user.setUsername(dto.username());

        // 2. CRIPTOGRAFAR a senha (Segurança nível WMS Orion)
        user.setPassword(passwordEncoder.encode(dto.password()));

        // 3. Salvar no banco
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}