package com.emadsolutions.LMS.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLogin {
    @NotEmpty(message = "username cannot be null or empty")
    private String username;
    @NotEmpty(message = "password cannot be null or empty")
    private String password;
}
