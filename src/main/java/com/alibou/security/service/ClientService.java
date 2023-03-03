package com.alibou.security.service;

import com.alibou.security.dto.ClientDto;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientDto create(ClientDto clientDto);
    Optional<ClientDto> findById(Integer id);
    List<ClientDto> findAll();
    ClientDto update(Integer id, ClientDto ClientDto);
    String deleteById(Integer id);
}
