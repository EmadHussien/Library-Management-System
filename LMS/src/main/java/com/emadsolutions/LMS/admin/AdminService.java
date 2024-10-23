package com.emadsolutions.LMS.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> getAdminByUsername(String username)
    {
        return adminRepository.findByUsername(username);
    }

    public Admin saveAdmin(Admin admin)
    {
        return adminRepository.save(admin);
    }

}
