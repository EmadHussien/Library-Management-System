package com.emadsolutions.LMS.auth;

import com.emadsolutions.LMS.DTOs.CustomResponse;
import com.emadsolutions.LMS.DTOs.UserLogin;
import com.emadsolutions.LMS.admin.Admin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Admin admin)
    {
        var createdAdmin =  authService.registerUser(admin);
        var response = new CustomResponse<>(CustomResponse.ResponseStatus.success, null, createdAdmin);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLogin userLoginData)
    {
        var tokenDetails = authService.handleLogin(userLoginData);
        return ResponseEntity.ok(new CustomResponse<>(CustomResponse.ResponseStatus.success, null, tokenDetails));
    }

}
