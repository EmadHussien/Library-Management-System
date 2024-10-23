package com.emadsolutions.LMS.auth;
import com.emadsolutions.LMS.DTOs.AdminToDTO;
import com.emadsolutions.LMS.DTOs.UserLogin;
import com.emadsolutions.LMS.Exceptions.ConflictException;
import com.emadsolutions.LMS.admin.Admin;
import com.emadsolutions.LMS.admin.AdminService;
import com.emadsolutions.LMS.security.CustomUserDetailsService;
import com.emadsolutions.LMS.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public AuthService(AdminService adminService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Map<String, Object> handleLogin(@Valid UserLogin userLoginData) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginData.getUsername(),
                            userLoginData.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginData.getUsername());
                String token = jwtTokenProvider.generateToken(userDetails);
                Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);
                Date issuedAt = jwtTokenProvider.getIssuedAtDateFromToken(token);
                long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
                String username = userDetails.getUsername();

                return getTokenDetails(token,expiresIn,issuedAt,username);
            } else {
                throw new BadCredentialsException("Authentication failed");
            }

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    private Map<String,Object> getTokenDetails(String token, long expiresIn,Date issuedAt,String username)
    {
        Map<String, Object> tokenMap = new HashMap<>();
        Map<String,String> userMap = new HashMap<>();

        tokenMap.put("token",token);
        tokenMap.put("expiresIn",expiresIn);
        tokenMap.put("issuedAt",issuedAt);
        tokenMap.put("type","Bearer");
        tokenMap.put("user",userMap);
        userMap.put("username", username);

        return tokenMap;
    }
    public AdminToDTO registerUser(Admin admin) {
        Optional<Admin> foundAdmin = adminService.getAdminByUsername(admin.getUsername());

        if (foundAdmin.isPresent())
            throw new ConflictException("Username is already taken");

        admin.setEncryptedPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminService.saveAdmin(admin);

        return new AdminToDTO(savedAdmin);
    }
}
