package io.pi.spring_ecom.model.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String email;
    private Long id;
}
