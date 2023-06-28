package com.crm.security.contoller;

import com.crm.security.dto.ClientDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app/clients")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> create(@RequestBody @Valid ClientDto clientDto, @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, EmailAlreadyExistsException {
        return ResponseEntity.ok(clientService.create(clientDto, userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException {
        ClientDto clientDto = clientService.findById(id, userDetails);
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        List<ClientDto> clientDtoList = clientService.findAll(userDetails);
        return ResponseEntity.ok(clientDtoList);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ClientDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize, @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        Page<ClientDto> clientDtoList = clientService.findAll(pageNumber, pageSize, userDetails);
        return ResponseEntity.ok(clientDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientDto>> search(@RequestParam("q") String query, @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        return ResponseEntity.ok(clientService.search(query, userDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable("id") Integer id, @RequestBody @Valid ClientDto clientDto, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException, UserNotFoundException, ClientNotFoundException, EmailAlreadyExistsException {
        return ResponseEntity.ok(clientService.update(id, clientDto, userDetails));
    }
}