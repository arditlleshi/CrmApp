package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateDto extends BaseUserDto {

    private List<Integer> roleIds;
}