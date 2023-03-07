package com.alibou.security.contoller.operator;

import com.alibou.security.dto.ClientDto;
import com.alibou.security.dto.UserClientDto;
import com.alibou.security.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operator/clients")
@RequiredArgsConstructor
@Component("operatorClientController")
public class ClientController {
    private final ClientService clientService;
    @PostMapping
    public ResponseEntity<ClientDto> create(@RequestBody UserClientDto userClientDto, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(clientService.create(userClientDto, userDetails));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        Optional<ClientDto> clientDto = clientService.findById(id, userDetails);
        return clientDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll(@AuthenticationPrincipal UserDetails userDetails){
        List<ClientDto> clientDtoList = clientService.findAll(userDetails);
        return ResponseEntity.ok(clientDtoList);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ClientDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize, @AuthenticationPrincipal UserDetails userDetails){
        Page<ClientDto> clientDtoList = clientService.findAll(pageNumber, pageSize, userDetails);
        return ResponseEntity.ok(clientDtoList);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ClientDto>> search(@RequestParam("q") String query, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(clientService.search(query, userDetails));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable("id") Integer id, @RequestBody ClientDto clientDto, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        return ResponseEntity.ok(clientService.update(id, clientDto, userDetails));
    }
}