package com.oxysystem.general.exception;

import lombok.Getter;

@Getter
public class GrabException extends RuntimeException{
    private final String errors;
    public GrabException(String message, String errors){
        super(message);
        this.errors = errors;
    }

}
