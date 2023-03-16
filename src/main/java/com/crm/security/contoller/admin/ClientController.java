package com.crm.security.contoller.admin;

import com.crm.security.dto.ClientDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/clients")
@Component("adminClientController")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> create(@RequestBody @Valid ClientDto clientDto) throws EmailAlreadyExistsException {
        return ResponseEntity.ok(clientService.create(clientDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Integer id) throws ClientNotFoundException {
        ClientDto clientDto = clientService.findById(id);
        return ResponseEntity.ok(clientDto);
    }
    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll(){
        List<ClientDto> clientDtoList = clientService.findAll();
        return ResponseEntity.ok(clientDtoList);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ClientDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<ClientDto> clientDtoList = clientService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(clientDtoList);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ClientDto>> search(@RequestParam("q") String query){
        return ResponseEntity.ok(clientService.search(query));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable("id") Integer id, @RequestBody @Valid ClientDto clientDto) throws ClientNotFoundException, EmailAlreadyExistsException {
        return ResponseEntity.ok(clientService.update(id, clientDto));
    }
}