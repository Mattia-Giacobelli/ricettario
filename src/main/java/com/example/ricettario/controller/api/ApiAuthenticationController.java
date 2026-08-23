package com.example.ricettario.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ricettario.DTO.UserApiDTO;
import com.example.ricettario.DTO.UserApiLoginDTO;
import com.example.ricettario.DTO.UserApiResponseDTO;
import com.example.ricettario.entities.Permission;
import com.example.ricettario.entities.User;
import com.example.ricettario.security.JwtUtil;
import com.example.ricettario.service.PermissionService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.utilities.PermissionType;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final PermissionService permissionService;
    private final JwtUtil jwtUtil;

    public ApiAuthenticationController(UserService userService, BCryptPasswordEncoder passwordEncoder,
            PermissionService permissionService, JwtUtil jwtUtil) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserApiDTO userDTO) {

        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username già in uso");
        }
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email già in uso");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // hash corretto, una sola volta

        Permission userPermission = permissionService.findByType(PermissionType.USER);
        user.setPermission(userPermission);

        userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApiLoginDTO userDTO) {

        if (!userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");
        }

        User user = userService.findByUsername(userDTO.getUsername());

        // matches() confronta la password in chiaro con l'hash salvato,
        // gestendo da solo il salt -- MAI ri-hashare la password in ingresso.
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata");
        }

        String permission = user.getPermission().getPermissionType().toString();
        String token = jwtUtil.generateToken(user.getUsername(), permission);

        UserApiResponseDTO response = new UserApiResponseDTO(token, user.getUsername(), permission);

        return ResponseEntity.ok(response); // <-- return presente, a differenza di prima
    }
}
