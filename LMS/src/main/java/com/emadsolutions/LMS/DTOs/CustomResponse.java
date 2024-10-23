package com.emadsolutions.LMS.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CustomResponse<T> {

    private ResponseStatus status;
    private String message;
    private T data; // This will work for both single objects and lists
    private Date timestamp;
    private Integer httpStatusCode; // Optional HTTP status code

    public CustomResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }

    public CustomResponse(ResponseStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = new Date();
    }


    public CustomResponse(ResponseStatus status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = (T) data;
        this.timestamp = new Date();
    }

    public CustomResponse(ResponseStatus status, String message, T data, Integer httpStatusCode) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.httpStatusCode = httpStatusCode;
        this.timestamp = new Date();
    }

    public enum ResponseStatus {
        success,
        failed
    }
}

