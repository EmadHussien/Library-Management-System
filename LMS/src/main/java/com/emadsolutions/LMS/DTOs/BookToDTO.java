package com.emadsolutions.LMS.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString


public class BookToDTO {

    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
    private Integer totalCopies;
    private int availableCopies;

}
