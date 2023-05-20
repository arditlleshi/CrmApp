package com.crm.security.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(Integer id){
        super("User not found with id: " + id);
    }

    public UserNotFoundException(String username){
        super("User not found with username: " + username);
    }
}