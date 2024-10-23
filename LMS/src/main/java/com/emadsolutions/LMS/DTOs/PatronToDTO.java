package com.emadsolutions.LMS.DTOs;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatronToDTO {
    private Long id;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;
    private int numOfBorrowedBooks;

}


