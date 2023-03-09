package com.crm.security.service.implementation;

import com.crm.security.dto.AuthenticationResponseDto;
import com.crm.security.dto.LoginRequestDto;
import com.crm.security.dto.UserRegisterDto;
import com.crm.security.dto.UserResponseDto;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Role;
import com.crm.security.model.User;
import com.crm.security.repository.RoleRepository;
import com.crm.security.repository.UserRepository;
import com.crm.security.security.JwtService;
import com.crm.security.security.UserDetailsServiceImpl;
import com.crm.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserDetailsServiceImpl userDetails;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final RoleRepository roleRepository;
    @Override
    public UserResponseDto create(UserRegisterDto userRegisterDto) {
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        User user = dtoToEntity(userRegisterDto);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        userRepository.save(user);
        return convertToResponseDto(user);
    }

    @Override
    public UserResponseDto findById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return convertToResponseDto(users);
    }
    @Override
    public Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return convertToResponseDto(users);
    }
    @Override
    public List<UserResponseDto> search(String query) {
        Specification<User> specification = ((root, query1, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("firstname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("lastname"), "%" + query + "%"),
                criteriaBuilder.like(root.get("email"), "%" + query + "%")
        ));
        return convertToResponseDto(userRepository.findAll(specification));
    }
    @Override
    public UserResponseDto update(Integer id, UserResponseDto userResponseDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (userRepository.findByEmail(userResponseDto.getEmail()).isPresent() && !Objects.equals(user.getEmail(), userResponseDto.getEmail())){
            throw new EmailAlreadyExistsException("Email is already taken!");
        }
        user.setFirstname(userResponseDto.getFirstname());
        user.setLastname(userResponseDto.getLastname());
        user.setEmail(userResponseDto.getEmail());
        userRepository.save(user);
        return convertToResponseDto(user);
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userDetails.loadUserByUsername(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }

    @Override
    public String deleteById(Integer id) {
        if (userRepository.findById(id).isEmpty()){
            return "User not found with id: " + id;
        }
        userRepository.deleteById(id);
        return "Successfully deleted user with id: " + id;
    }
    @Override
    public User findUserByEmailOrThrowException(UserDetails userDetails){
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername()));
    }

    private User dtoToEntity(UserRegisterDto userRegisterDto) {
        User user = mapper.map(userRegisterDto, User.class);
        List<Role> roles = userRegisterDto.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }
    private UserResponseDto convertToResponseDto(User user) {
        UserResponseDto userResponseDto = mapper.map(user, UserResponseDto.class);
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::toString)
                .collect(Collectors.toList());
        userResponseDto.setRole(roleNames);
        return userResponseDto;
    }
    private List<UserResponseDto> convertToResponseDto(List<User> userList) {
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
