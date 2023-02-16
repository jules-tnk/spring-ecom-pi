package io.pi.spring_ecom.model.request;

import lombok.Data;

@Data
public class SingleOrderRequest {
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
}
