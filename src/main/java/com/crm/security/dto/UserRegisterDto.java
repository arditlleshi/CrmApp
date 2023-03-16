package com.crm.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegisterDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between {min} and {max} characters")
    private String firstname;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "First name must be between {min} and {max} characters")
    private String lastname;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    private String password;
    private List<Integer> roleIds;
}
