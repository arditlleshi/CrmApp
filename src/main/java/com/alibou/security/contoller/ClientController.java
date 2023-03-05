package com.alibou.security.contoller;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(clientService.create(clientDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Integer id){
        Optional<ClientDto> clientDto = clientService.findById(id);
        return clientDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll(){
        List<ClientDto> clientDtoList = clientService.findAll();
        return ResponseEntity.ok(clientDtoList);
    }
}