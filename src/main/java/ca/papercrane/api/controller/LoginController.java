package ca.papercrane.api.controller;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.security.login.LoginRequest;
import ca.papercrane.api.security.login.LoginResponse;
import ca.papercrane.api.service.impl.LoginServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginServiceImpl service;

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            val response = service.authenticateAndBuildResponse(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}