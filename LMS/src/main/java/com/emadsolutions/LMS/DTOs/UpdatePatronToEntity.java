package com.emadsolutions.LMS.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePatronToEntity {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @Pattern(regexp = "^(010|011|015|012)\\d{8}$", message = "Phone number must start with 010, 011, 015, or 012 and be 11 digits long")
    private String phoneNumber;

    @Email(message = "Email should be valid ex: example@example.com")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String address;

}
