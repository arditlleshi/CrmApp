package com.crm.security.service;

import com.crm.security.dto.ClientDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ClientService {

    Client findClientByIdOrThrowException(Integer id) throws ClientNotFoundException;

    ClientDto create(ClientDto clientDto, UserDetails userDetails) throws EmailAlreadyExistsException, UserNotFoundException;

    ClientDto findById(Integer id, UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException;

    List<ClientDto> findAll(UserDetails userDetails) throws UserNotFoundException;

    ClientDto update(Integer id, ClientDto clientDto, UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException, EmailAlreadyExistsException;

    Page<ClientDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) throws UserNotFoundException;

    List<ClientDto> search(String query, UserDetails userDetails) throws UserNotFoundException;
}
