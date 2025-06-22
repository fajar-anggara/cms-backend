package com.backendapp.cms.common.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Builder
@Getter
@Setter
public class ErrorResponse {

    private boolean success;
    private String message;
    private HashMap<String, String> errors;

    public ErrorResponse(boolean b, String message) {
        this.success = b;
        this.message = message;
    }
    public ErrorResponse(boolean b, String message, HashMap<String, String> errors) {
        this.success = b;
        this.message = message;
        this.errors = errors;
    }
}
