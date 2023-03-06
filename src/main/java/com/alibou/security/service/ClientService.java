package com.alibou.security.service;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.dto.UserClientDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientDto create(ClientDto clientDto);
    Optional<ClientDto> findById(Integer id);
    List<ClientDto> findAll();
    ClientDto update(Integer id, ClientDto ClientDto);
    String deleteById(Integer id);
    ClientDto create(UserClientDto userClientDto, UserDetails userDetails);
    Optional<ClientDto> findById(Integer id, UserDetails userDetails) throws AccessDeniedException;
    List<ClientDto> findAll(UserDetails userDetails);
    ClientDto update(Integer id, ClientDto ClientDto, UserDetails userDetails);
    String deleteById(Integer id, UserDetails userDetails);
}
