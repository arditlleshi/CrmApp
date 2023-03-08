package com.crm.security.service;

import com.crm.security.dto.ClientDto;
import com.crm.security.dto.UserClientDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ClientService {
    ClientDto create(ClientDto clientDto);
    ClientDto findById(Integer id);
    List<ClientDto> findAll();
    Page<ClientDto> findAll(Integer pageNumber, Integer pageSize);
    ClientDto update(Integer id, ClientDto clientDto);
    List<ClientDto> search(String query);
    ClientDto create(UserClientDto userClientDto, UserDetails userDetails);
    ClientDto findById(Integer id, UserDetails userDetails) throws AccessDeniedException;
    List<ClientDto> findAll(UserDetails userDetails);
    ClientDto update(Integer id, ClientDto clientDto, UserDetails userDetails) throws AccessDeniedException;
    Page<ClientDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails);
    List<ClientDto> search(String query, UserDetails userDetails);
}
