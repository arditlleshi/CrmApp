package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto extends BaseUserDto {

    private Integer id;

    private Integer userId;
}
