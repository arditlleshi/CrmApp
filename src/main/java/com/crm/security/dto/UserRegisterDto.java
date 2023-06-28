package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegisterDto extends BaseUserDto {

    private String password;

    private List<Integer> roleIds;
}
