package com.edu.flysixbackend.dto;

import org.springframework.http.HttpStatus;

public class ResponseDto {

    private AuthUserDto user;
    private String token;
    private HttpStatus httpStatus;

    public ResponseDto(AuthUserDto user, String token, HttpStatus httpStatus) {
        this.user = user;
        this.token = token;
        this.httpStatus = httpStatus;
    }

    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
