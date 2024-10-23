package com.emadsolutions.LMS.security;
import com.emadsolutions.LMS.admin.Admin;
import com.emadsolutions.LMS.admin.AdminService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminService adminService;

    public CustomUserDetailsService(AdminService adminService) {
        this.adminService = adminService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getAdminByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getUsername())
                .password(admin.getEncryptedPassword())
                .roles("Admin")
                .build();
    }
}
