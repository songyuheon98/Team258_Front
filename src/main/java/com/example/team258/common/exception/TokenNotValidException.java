package com.example.team258.common.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String msg){
        super(msg);
    }
}
