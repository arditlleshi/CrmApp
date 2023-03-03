package com.alibou.security.service.implementation;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.model.Client;
import com.alibou.security.model.User;
import com.alibou.security.repository.ClientRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImplementation implements ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    @Override
    public ClientDto create(ClientDto clientDto) {
        if (clientRepository.findByEmail(clientDto.getEmail()).isPresent()){
            throw new IllegalStateException("Email is already taken!");
        }
        Client client = dtoToEntity(clientDto);
        clientRepository.save(client);
        return convertToResponseDto(client);
    }

    @Override
    public Optional<ClientDto> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<ClientDto> findAll() {
        return null;
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto) {
        return null;
    }

    @Override
    public String deleteById(Integer id) {
        return null;
    }
    public Client dtoToEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstname(clientDto.getFirstname());
        client.setLastname(clientDto.getLastname());
        client.setEmail(clientDto.getEmail());
        User user = userRepository.findById(clientDto.getUserId()).orElseThrow(() ->
                new UsernameNotFoundException("User not found with id: " + clientDto.getUserId()));
        client.setUser(user);
        return client;
    }
    public ClientDto convertToResponseDto(Client client) {
        ClientDto clientDto = mapper.map(client, ClientDto.class);
        Integer userId = client.getUser().getId();
        clientDto.setUserId(userId);
        return clientDto;
    }
}