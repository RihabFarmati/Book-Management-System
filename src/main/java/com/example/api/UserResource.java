package com.example.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.dtos.UserDTO;
import com.example.mappers.UserMapper;
import com.example.models.Role;
import com.example.models.User;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.utils.AuthenticationConstants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    private UserMapper userMapper;

    @Autowired
    public UserResource(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUserName(username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<UserDTO> saveUser(@Valid @RequestBody() UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return ResponseEntity.badRequest().body(null);
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        try {
            User user = userService.saveUser(userMapper.toEntity(userDTO), userDTO.getRoleName());
            return ResponseEntity.created(uri).body(userMapper.toDto(user));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data", e);
        }
    }

    @GetMapping(value = "user/{username}")
    public ResponseEntity<User> getUser(@PathVariable() String username) {
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @GetMapping(value = "userid/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable() Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/add")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    private void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new RuntimeException("Refresh token is missing");
        }

        try {
            String refreshToken = authorizationHeader.substring(BEARER.length());
            Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            User user = userService.getUser(username);

            String accessToken = generateAccessToken(user, request, algorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put(ACCESS_TOKEN, accessToken);
            tokens.put(REFRESH_TOKEN, refreshToken);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (Exception exception) {
            response.setHeader("error", exception.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);

            Map<String, String> error = new HashMap<>();
            error.put("error_message", exception.getMessage());
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

    private String generateAccessToken(User user, HttpServletRequest request, Algorithm algorithm) {
        return JWT.create()
                .withSubject(user.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
    }
}

