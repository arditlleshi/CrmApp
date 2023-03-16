package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterResponseDto extends BaseUserDto{
    private Integer id;
    private String token;
}
