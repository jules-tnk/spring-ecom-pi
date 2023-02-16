package io.pi.spring_ecom.model.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderResponse {
    private LocalDate date;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
