package com.crm.security.dto;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class BaseUserDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between {min} and {max} characters")
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "First name must be between {min} and {max} characters")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
}
