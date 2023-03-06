package com.alibou.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Integer userId;
}
