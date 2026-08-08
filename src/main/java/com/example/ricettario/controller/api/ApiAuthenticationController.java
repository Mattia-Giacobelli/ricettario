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
import com.example.ricettario.DTO.UserApiResponseDTO;
import com.example.ricettario.entities.Permission;
import com.example.ricettario.entities.User;
import com.example.ricettario.service.PermissionService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.utilities.PermissionType;

@RestController
@RequestMapping("/ricettario")
public class ApiAuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final PermissionService permissionService;

    public ApiAuthenticationController(UserService userService, BCryptPasswordEncoder passwordEncoder,
            PermissionService permissionService) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserApiDTO userDTO) {

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        // Controllo unicità username/email (oltre al vincolo UNIQUE nel DB)
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.IM_USED).body("Username già in uso");
        }
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.IM_USED).body("Username già in uso");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Permission userPermission = permissionService.findByType(PermissionType.USER);
        user.setPermission(userPermission);

        userService.create(user);

        return ResponseEntity.ok("Utente registrato con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApiDTO userDTO) {

        User user;

        if (userService.existsByUsername(userDTO.getUsername())) {

            user = userService.findByUsername(userDTO.getUsername());

            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            if (user.getPassword().equals(userDTO.getPassword())) {

                UserApiResponseDTO resUser = new UserApiResponseDTO();

                resUser.setUsername(user.getUsername());
                resUser.setPermission(user.getPermission().getPermissionType().toString());

                ResponseEntity.status(HttpStatus.ACCEPTED).body(resUser);

            } else {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata");

            }

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");

    }

}
