package io.pi.spring_ecom.model.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long clientId;
    private List<SingleOrderRequest> orders;
}
