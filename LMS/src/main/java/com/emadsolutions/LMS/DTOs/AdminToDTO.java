package com.emadsolutions.LMS.DTOs;


import com.emadsolutions.LMS.admin.Admin;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class AdminToDTO {
    private Long id;
    private String username;

    public AdminToDTO(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getUsername();
    }
}
