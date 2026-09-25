package mk.smarthome.homeprocessor.controller;

import lombok.RequiredArgsConstructor;
import mk.smarthome.homeprocessor.dto.AuthRequest;
import mk.smarthome.homeprocessor.dto.AuthResponse;
import mk.smarthome.homeprocessor.model.User;
import mk.smarthome.homeprocessor.repository.UserRepository;
import mk.smarthome.homeprocessor.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name(), user.getApartmentId()
        );

        return ResponseEntity.ok(new AuthResponse(
                token, user.getUsername(), user.getRole().name(), user.getApartmentId()
        ));
    }

}