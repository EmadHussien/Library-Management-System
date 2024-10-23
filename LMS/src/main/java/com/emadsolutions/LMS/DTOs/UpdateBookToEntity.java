package com.emadsolutions.LMS.DTOs;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UpdateBookToEntity {
    @NotBlank(message = "Title is mandatory")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 50, message = "Author's name should not exceed 50 characters")
    private String author;

    @NotNull(message = "Publication year is required")
    private Integer publicationYear;

    @NotEmpty(message = "ISBN is mandatory")
    @Size(max = 20, message = "ISBN should not exceed 20 characters")
    private String isbn;

    @NotNull(message = "Total number of copies is required")
    @Min(value = 1, message = "Total number of copies must be greater than 0")
    private Integer totalCopies;
}
