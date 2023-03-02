package com.alibou.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private List<String> roleNames;
}
