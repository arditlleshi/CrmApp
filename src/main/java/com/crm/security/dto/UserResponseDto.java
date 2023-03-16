package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto extends BaseUserDto{
    private Integer id;
    private List<String> role;
}
