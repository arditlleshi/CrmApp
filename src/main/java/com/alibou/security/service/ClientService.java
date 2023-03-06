package com.alibou.security.service;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.dto.UserClientDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientDto create(ClientDto clientDto);
    ClientDto create(UserClientDto userClientDto, UserDetails userDetails);
    Optional<ClientDto> findById(Integer id);
    List<ClientDto> findAll();
    ClientDto update(Integer id, ClientDto ClientDto);
    String deleteById(Integer id);
}
