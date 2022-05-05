package com.edu.flysixbackend.dto;

import org.springframework.http.HttpStatus;

public class ErrorDto {

    private String error;
    private HttpStatus status;

    public ErrorDto() {
    }

    public ErrorDto(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
