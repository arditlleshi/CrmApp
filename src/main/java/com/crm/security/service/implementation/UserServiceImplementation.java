package com.crm.security.service.implementation;

import com.crm.security.dto.*;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Role;
import com.crm.security.model.Token;
import com.crm.security.model.User;
import com.crm.security.repository.RoleRepository;
import com.crm.security.repository.TokenRepository;
import com.crm.security.repository.UserRepository;
import com.crm.security.security.JwtService;
import com.crm.security.security.UserDetailsServiceImpl;
import com.crm.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.crm.security.enums.TokenType.BEARER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserDetailsServiceImpl userDetails;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;

    @Override
    public UserRegisterResponseDto register(UserRegisterDto userRegisterDto) throws EmailAlreadyExistsException{
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        User user = dtoToEntity(userRegisterDto);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(userDetails.loadUserByUsername(user.getEmail()));
        String refreshToken = jwtService.generateRefreshToken(userDetails.loadUserByUsername(user.getEmail()));
        saveUserToken(user, jwtToken);
        UserRegisterResponseDto userRegisterResponseDto = mapper.map(user, UserRegisterResponseDto.class);
        userRegisterResponseDto.setAccessToken(jwtToken);
        userRegisterResponseDto.setRefreshToken(refreshToken);
        return userRegisterResponseDto;
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) throws UserNotFoundException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
                                          );
        UserDetails userDetail = userDetails.loadUserByUsername(request.getEmail());
        var jwtToken = jwtService.generateToken(userDetail);
        var refreshToken = jwtService.generateRefreshToken(userDetail);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User not found with email: " + request.getEmail()));
        revokeAllUserTokens(user);
        saveUserToken(findUserByEmailOrThrowException(userDetail), jwtToken);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UserNotFoundException{
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
            UserDetails userDetails1 = new org.springframework.security.core.userdetails.User(userEmail, user.getPassword(), user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));

            if (jwtService.isTokenValid(refreshToken, userDetails1)) {
                var accessToken = jwtService.generateToken(userDetails1);
                revokeAllUserTokens(user);
                saveUserToken(findUserByEmailOrThrowException(userDetails1), accessToken);
                var authResponse = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(User user, String jwtToken){
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(BEARER);
        token.setExpired(false);
        token.setExpired(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public UserResponseDto findById(Integer id) throws UserNotFoundException{
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToResponseDto(user);
    }

    @Override
    public List<UserResponseDto> findAll(){
        List<User> users = userRepository.findAll();
        return convertToResponseDto(users);
    }

    @Override
    public Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return convertToResponseDto(users);
    }

    @Override
    public List<UserResponseDto> search(String query){
        Specification<User> specification = ((root, query1, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("firstname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("lastname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("email"), "%" + query + "%")
                                                                                                  ));
        return convertToResponseDto(userRepository.findAll(specification));
    }

    @Override
    public UserResponseDto update(Integer id, UserUpdateDto userUpdateDto) throws UserNotFoundException, EmailAlreadyExistsException{
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (userRepository.findByEmail(userUpdateDto.getEmail()).isPresent() && !Objects.equals(user.getEmail(), userUpdateDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        user.setFirstname(userUpdateDto.getFirstname());
        user.setLastname(userUpdateDto.getLastname());
        user.setEmail(userUpdateDto.getEmail());
        List<Role> roles = userUpdateDto.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId)))
                .toList();
        user.setRoles(roles);
        userRepository.save(user);
        return convertToResponseDto(user);
    }

    @Override
    public String deleteById(Integer id){
        if (userRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return "Successfully deleted user with id: " + id;
    }

    @Override
    public User findUserByEmailOrThrowException(UserDetails userDetails) throws UserNotFoundException{
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername()));
    }

    @Override
    public boolean isUserAdmin(User user){
        List<String> userRoles = user.getRoles().stream().map(role -> role.getName().toString()).toList();
        return userRoles.contains("ROLE_ADMIN");
    }

    private User dtoToEntity(UserRegisterDto userRegisterDto){
        User user = mapper.map(userRegisterDto, User.class);
        List<Role> roles = userRegisterDto.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }

    private UserResponseDto convertToResponseDto(User user){
        UserResponseDto userResponseDto = mapper.map(user, UserResponseDto.class);
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::toString)
                .collect(Collectors.toList());
        userResponseDto.setRole(roleNames);
        return userResponseDto;
    }

    private List<UserResponseDto> convertToResponseDto(List<User> userList){
        return userList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private Page<UserResponseDto> convertToResponseDto(Page<User> users){
        List<UserResponseDto> userResponseDtoList = users.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(userResponseDtoList, users.getPageable(), users.getTotalElements());
    }
}
