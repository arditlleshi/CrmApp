package com.crm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDto {

    private Integer productId;

    private Integer orderId;

    private Double quantity;
}
