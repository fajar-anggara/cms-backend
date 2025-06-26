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

    public ErrorResponse(boolean isSuccessBool, String message) {
        this.success = isSuccessBool;
        this.message = message;
    }
    public ErrorResponse(boolean isSuccessBool, String message, HashMap<String, String> errors) {
        this.success = isSuccessBool;
        this.message = message;
        this.errors = errors;
    }
}
