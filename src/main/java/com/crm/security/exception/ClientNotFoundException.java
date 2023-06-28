package com.crm.security.exception;

public class ClientNotFoundException extends Exception {

    public ClientNotFoundException(Integer id) {
        super("Client not found with id: " + id);
    }
}