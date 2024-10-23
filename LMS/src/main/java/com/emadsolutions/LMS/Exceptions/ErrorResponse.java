package com.emadsolutions.LMS.Exceptions;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private int statusCode;
    private String message;
}
