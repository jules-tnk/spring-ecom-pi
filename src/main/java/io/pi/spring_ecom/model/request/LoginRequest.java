package io.pi.spring_ecom.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
