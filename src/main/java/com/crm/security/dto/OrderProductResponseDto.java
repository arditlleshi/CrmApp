package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductResponseDto {

    private Integer id;

    private Integer productId;

    private Integer orderId;

    private Double quantity;

    private Double amount;
}
