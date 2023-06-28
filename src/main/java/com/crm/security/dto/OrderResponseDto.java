package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {

    private Integer id;

    private Integer userId;

    private Integer clientId;

    private Double amount;
}
