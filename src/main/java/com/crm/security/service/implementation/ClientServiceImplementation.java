package com.crm.security.service.implementation;

import com.crm.security.dto.ClientDto;
import com.crm.security.dto.UserClientDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.model.Client;
import com.crm.security.model.User;
import com.crm.security.repository.ClientRepository;
import com.crm.security.repository.UserRepository;
import com.crm.security.service.ClientService;
import com.crm.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImplementation implements ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final UserService userService;
    @Override
    public ClientDto create(ClientDto clientDto) {
        if (clientRepository.findByEmail(clientDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        Client client = dtoToEntity(clientDto);
        clientRepository.save(client);
        return convertToResponseDto(client);
    }

    @Override
    public ClientDto findById(Integer id) {
        Client client = findClientByIdOrThrowException(id);
        return convertToResponseDto(client);
    }

    @Override
    public Client findClientByIdOrThrowException(Integer id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new ClientNotFoundException("Client not found with id: " + id)
        );
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
    public List<ClientDto> search(String query) {
        Specification<Client> specification = ((root, query1, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("firstname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("lastname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("email"), "%" + query + "%")
        ));
        return convertToResponseDto(clientRepository.findAll(specification));
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto) {
        Client client = findClientByIdOrThrowException(id);
        client.setFirstname(clientDto.getFirstname());
        client.setLastname(clientDto.getLastname());
        if (clientRepository.findByEmail(clientDto.getEmail()).isEmpty() || Objects.equals(client.getEmail(), clientDto.getEmail())){
            client.setEmail(clientDto.getEmail());
        }else {
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        clientRepository.save(client);
        return convertToResponseDto(client);

    }

    @Override
    public ClientDto create(UserClientDto userClientDto, UserDetails userDetails) {
        if (clientRepository.findByEmail(userClientDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Client client = new Client();
        client.setFirstname(userClientDto.getFirstname());
        client.setLastname(userClientDto.getLastname());
        client.setEmail(userClientDto.getEmail());
        client.setUser(user);
        clientRepository.save(client);
        return convertToResponseDto(client);
    }
    @Override
    public ClientDto findById(Integer id, UserDetails userDetails) throws AccessDeniedException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Client client = findClientByIdOrThrowException(id);
        if (!client.getUser().equals(user)){
            throw new AccessDeniedException("You don't have access to view client with id: " + id);
        }
        return convertToResponseDto(client);
    }

    @Override
    public List<ClientDto> findAll(UserDetails userDetails) {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        List<Client> clients = clientRepository.findAllByUser(user);
        return convertToResponseDto(clients);
    }
    @Override
    public Page<ClientDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Client> clients = clientRepository.findAllByUser(user, pageable);
        return convertToResponseDto(clients);
    }

    @Override
    public List<ClientDto> search(String query, UserDetails userDetails) {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Specification<Client> specification = Specification.where((root, query1, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("firstname"), "%" + query + "%"),
                                criteriaBuilder.like(root.get("lastname"), "%" + query + "%"),
                                criteriaBuilder.like(root.get("email"), "%" + query + "%")
                        ),
                        criteriaBuilder.equal(root.get("user"), user)
                )
        );
        return convertToResponseDto(clientRepository.findAll(specification));
    }

    @Override
    public ClientDto update(Integer id, ClientDto clientDto, UserDetails userDetails) throws AccessDeniedException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Client client = findClientByIdOrThrowException(id);
        if (!client.getUser().equals(user)){
            throw new AccessDeniedException("You don't have access to update client with id: " + id);
        }
        client.setFirstname(clientDto.getFirstname());
        client.setLastname(clientDto.getLastname());
        if (clientRepository.findByEmail(clientDto.getEmail()).isEmpty() || Objects.equals(client.getEmail(), clientDto.getEmail())){
            client.setEmail(clientDto.getEmail());
        }else {
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        clientRepository.save(client);
        return convertToResponseDto(client);
    }
    private Client dtoToEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstname(clientDto.getFirstname());
        client.setLastname(clientDto.getLastname());
        client.setEmail(clientDto.getEmail());
        User user = userRepository.findById(clientDto.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + clientDto.getUserId()));
        client.setUser(user);
        return client;
    }
    private ClientDto convertToResponseDto(Client client) {
        ClientDto clientDto = mapper.map(client, ClientDto.class);
        clientDto.setUserId(client.getUser().getId());
        return clientDto;
    }
    private List<ClientDto> convertToResponseDto(List<Client> clients) {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients){
            clientDtoList.add(convertToResponseDto(client));
        }
        return clientDtoList;
    }
    private Page<ClientDto> convertToResponseDto(Page<Client> clients){
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clients.getContent()){
            clientDtoList.add(convertToResponseDto(client));
        }
        return new PageImpl<>(clientDtoList, clients.getPageable(), clients.getTotalElements());
    }
}