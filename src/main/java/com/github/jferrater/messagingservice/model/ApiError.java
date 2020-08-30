package com.github.jferrater.messagingservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApiError {

    @Schema(description = "The error code")
    private int code;
    @Schema(description = "The error status")
    private String status;
    @Schema(description = "The error message")
    private String message;

    public ApiError(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
