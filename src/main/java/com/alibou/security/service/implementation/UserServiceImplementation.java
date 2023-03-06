package com.alibou.security.service.implementation;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import com.alibou.security.model.Role;
import com.alibou.security.model.User;
import com.alibou.security.repository.RoleRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.security.JwtService;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;
    @Override
    public UserResponseDto create(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            throw new IllegalStateException("Email is already taken!");
        }
        User user = dtoToEntity(userRegisterDto);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(user);
        return convertToResponseDto(user);
    }

    @Override
    public Optional<UserResponseDto> findById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id: " + id)));
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return convertToResponseDto(users);
    }

    @Override
    public UserResponseDto update(Integer id, UserResponseDto userResponseDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFirstname(userResponseDto.getFirstname());
            user.setLastname(userResponseDto.getLastname());
            if (userRepository.findByEmail(userResponseDto.getEmail()).isEmpty() || Objects.equals(user.getEmail(), userResponseDto.getEmail())){
                user.setEmail(userResponseDto.getEmail());
            }else {
                throw new IllegalStateException("Email is taken!");
            }
            userRepository.save(user);
            return convertToResponseDto(user);
        }else {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public String deleteById(Integer id) {
        if (userRepository.findById(id).isEmpty()){
            return "User not found with id: " + id;
        }else {
            userRepository.deleteById(id);
            return "Successfully deleted user with id: " + id;
        }
    }

    public User dtoToEntity(UserRegisterDto userRegisterDto) {
        User user = mapper.map(userRegisterDto, User.class);
        List<Role> roles = userRegisterDto.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id " + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }
    public UserResponseDto convertToResponseDto(User user) {
        UserResponseDto responseDto = mapper.map(user, UserResponseDto.class);
        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList());
        responseDto.setRoleNames(roleNames);
        return responseDto;
    }
    public List<UserResponseDto> convertToResponseDto(List<User> userList) {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : userList) {
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setId(user.getId());
            userResponseDto.setFirstname(user.getFirstname());
            userResponseDto.setLastname(user.getLastname());
            userResponseDto.setEmail(user.getEmail());
            List<String> roleNames = user.getRoles().stream()
                    .map(role -> role.getName().toString())
                    .collect(Collectors.toList());
            userResponseDto.setRoleNames(roleNames);
            userResponseDtoList.add(userResponseDto);
        }
        return userResponseDtoList;
    }
}
