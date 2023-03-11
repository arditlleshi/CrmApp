package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegisterDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private List<Integer> roleIds;
}
