package com.example.team258.common.exception;

public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException(String msg){
        super(msg);
    }
}
