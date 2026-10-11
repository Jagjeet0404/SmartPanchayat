package com.example.demo;

import com.example.demo.dto.AuthRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest req) {
        String email = req.getEmail().trim().toLowerCase();
        if (req.getName() == null || req.getName().isBlank()) {
            return error(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (users.findByEmail(email).isPresent()) {
            return error(HttpStatus.CONFLICT, "User already exists");
        }
        User u = new User();
        u.setName(req.getName().trim());
        u.setEmail(email);
        u.setPhone(req.getPhone());
        u.setPassword(encoder.encode(req.getPassword()));
        users.save(u);
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req) {
        Optional<User> found = users.findByEmail(req.getEmail().trim().toLowerCase());
        if (found.isEmpty()) {
            return error(HttpStatus.UNAUTHORIZED, "User not found. Please register first");
        }
        User u = found.get();
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            return error(HttpStatus.UNAUTHORIZED, "Wrong password");
        }
        return ResponseEntity.ok(Map.of("message", "Login successful", "name", u.getName(), "email", u.getEmail()));
    }

    static ResponseEntity<Map<String, String>> error(HttpStatus s, String msg) {
        return ResponseEntity.status(s).body(Map.of("error", msg));
    }
}
