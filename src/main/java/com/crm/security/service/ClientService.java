package com.crm.security.service;

import com.crm.security.dto.ClientDto;
import com.crm.security.dto.UserClientDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ClientService {
    ClientDto create(ClientDto clientDto) throws EmailAlreadyExistsException;
    ClientDto findById(Integer id) throws ClientNotFoundException;
    Client findClientByIdOrThrowException(Integer id) throws ClientNotFoundException;
    List<ClientDto> findAll();
    Page<ClientDto> findAll(Integer pageNumber, Integer pageSize);
    ClientDto update(Integer id, ClientDto clientDto) throws ClientNotFoundException, EmailAlreadyExistsException;
    List<ClientDto> search(String query);
    ClientDto create(UserClientDto userClientDto, UserDetails userDetails) throws EmailAlreadyExistsException, UserNotFoundException;
    ClientDto findById(Integer id, UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException;
    List<ClientDto> findAll(UserDetails userDetails) throws UserNotFoundException;
    ClientDto update(Integer id, ClientDto clientDto, UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException, EmailAlreadyExistsException;
    Page<ClientDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) throws UserNotFoundException;
    List<ClientDto> search(String query, UserDetails userDetails) throws UserNotFoundException;
}
