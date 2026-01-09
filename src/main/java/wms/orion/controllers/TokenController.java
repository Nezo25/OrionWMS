package wms.orion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wms.orion.dto.user.LoginRequestDTO;
import wms.orion.dto.user.LoginResponseDTO;
import wms.orion.models.repositories.UserRepository;

import java.time.Instant;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {

        // 1. Busca o usuário no banco
        var user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Valida a senha (BCrypt)
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        // 3. Prepara o Payload do JWT (Claims)
        var now = Instant.now();
        var expiresIn = 3600L; // 1 hora de validade

        var claims = JwtClaimsSet.builder()
                .issuer("wms-orion")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("username", user.getUsername())
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));
    }
}