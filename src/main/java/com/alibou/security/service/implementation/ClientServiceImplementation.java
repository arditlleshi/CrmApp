package com.alibou.security.service.implementation;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.dto.UserClientDto;
import com.alibou.security.model.Client;
import com.alibou.security.model.User;
import com.alibou.security.repository.ClientRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
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
        return Optional.ofNullable(clientRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new UsernameNotFoundException("Client not found with id: " + id)));
    }

    @Override
    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        return convertToResponseDto(clients);
    }

    @Override
    public Page<ClientDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Client> clients = clientRepository.findAll(pageable);
        return convertToResponseDto(clients);
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()){
            Client client = optionalClient.get();
            client.setFirstname(clientDto.getFirstname());
            client.setLastname(clientDto.getLastname());
            if (clientRepository.findByEmail(clientDto.getEmail()).isEmpty() || Objects.equals(client.getEmail(), clientDto.getEmail())){
                client.setEmail(clientDto.getEmail());
            }else {
                throw new IllegalStateException("Email is taken!");
            }
            clientRepository.save(client);
            return convertToResponseDto(client);
        }else {
            throw new UsernameNotFoundException("Client not found with id: " + id);
        }
    }
    @Override
    public ClientDto create(UserClientDto userClientDto, UserDetails userDetails) {
        if (clientRepository.findByEmail(userClientDto.getEmail()).isPresent()){
            throw new IllegalStateException("Email is already taken!");
        }
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Client client = new Client();
        client.setFirstname(userClientDto.getFirstname());
        client.setLastname(userClientDto.getLastname());
        client.setEmail(userClientDto.getEmail());
        client.setUser(user);
        clientRepository.save(client);
        return convertToResponseDto(client);
    }
    @Override
    public Optional<ClientDto> findById(Integer id, UserDetails userDetails) throws AccessDeniedException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Client not found with id: " + id)
        );
        if (!client.getUser().equals(user)){
            throw new AccessDeniedException("You don't have access to view client with id: " + id);
        }
        return Optional.of(convertToResponseDto(client));
    }

    @Override
    public List<ClientDto> findAll(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Client> clients = clientRepository.findAllByUser(user);
        return convertToResponseDto(clients);
    }
    @Override
    public Page<ClientDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Client> clients = clientRepository.findAllByUser(user, pageable);
        return convertToResponseDto(clients);
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto, UserDetails userDetails) throws AccessDeniedException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Client not found with id: " + id)
        );
        if (!client.getUser().equals(user)){
            throw new AccessDeniedException("You don't have access to update client with id: " + id);
        }
        client.setFirstname(clientDto.getFirstname());
        client.setLastname(clientDto.getLastname());
        if (clientRepository.findByEmail(clientDto.getEmail()).isEmpty() || Objects.equals(client.getEmail(), clientDto.getEmail())){
            client.setEmail(clientDto.getEmail());
        }else {
            throw new IllegalStateException("Email is taken!");
        }
        clientRepository.save(client);
        return convertToResponseDto(client);
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
    private ClientDto convertToResponseDto(Client client) {
        ClientDto clientDto = mapper.map(client, ClientDto.class);
        Integer userId = client.getUser().getId();
        clientDto.setUserId(userId);
        return clientDto;
    }
    private List<ClientDto> convertToResponseDto(List<Client> clients) {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients){
            ClientDto clientDto = new ClientDto();
            clientDto.setId(client.getId());
            clientDto.setFirstname(client.getFirstname());
            clientDto.setLastname(client.getLastname());
            clientDto.setEmail(client.getEmail());
            Integer userId = client.getUser().getId();
            clientDto.setUserId(userId);
            clientDtoList.add(clientDto);
        }
        return clientDtoList;
    }
    private Page<ClientDto> convertToResponseDto(Page<Client> clients){
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients.getContent()){
            ClientDto clientDto = new ClientDto();
            clientDto.setId(client.getId());
            clientDto.setFirstname(client.getFirstname());
            clientDto.setLastname(client.getLastname());
            clientDto.setEmail(client.getEmail());
            Integer userId = client.getUser().getId();
            clientDto.setUserId(userId);
            clientDtoList.add(clientDto);
        }
        return new PageImpl<>(clientDtoList, clients.getPageable(), clients.getTotalElements());
    }
}