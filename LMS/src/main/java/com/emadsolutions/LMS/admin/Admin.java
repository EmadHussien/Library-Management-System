package com.emadsolutions.LMS.admin;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue()
    private Long id;

    @Size(min = 8, message = "Username must be at least 8 characters long")
    @NotEmpty(message="Username cannot be empty")
    @Column(name = "username")
    private String username;

    @Transient
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character")
    @NotEmpty(message="Password cannot be empty")
    private String password;

    @Column(name ="password")
    private String encryptedPassword;

}
