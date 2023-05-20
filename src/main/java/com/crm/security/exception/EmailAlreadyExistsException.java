package com.crm.security.exception;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String email){
        super("Email [" + email + "] is already taken!");
    }
}