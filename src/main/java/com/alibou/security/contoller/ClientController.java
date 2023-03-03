package com.alibou.security.contoller;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
        return ResponseEntity.ok(clientService.create(clientDto));
    }
}