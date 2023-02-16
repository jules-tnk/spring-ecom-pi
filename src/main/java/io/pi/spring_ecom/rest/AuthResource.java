package io.pi.spring_ecom.rest;

import io.pi.spring_ecom.model.AppUserDTO;
import io.pi.spring_ecom.model.request.LoginRequest;
import io.pi.spring_ecom.model.responses.LoginResponse;
import io.pi.spring_ecom.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final AppUserService appUserService;

    public AuthResource(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        AppUserDTO appUserDTO = appUserService.getByEmail(loginRequest.getEmail());

        if (appUserDTO == null){
            return ResponseEntity.notFound().build();
        }

        if (!appUserDTO.getPassword().equals(loginRequest.getPassword())){
            return ResponseEntity.notFound().build();
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(appUserDTO.getEmail());
        loginResponse.setId(appUserDTO.getId());
        return ResponseEntity.ok(loginResponse);

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUserDTO appUserDTO){
        if (appUserService.existsByEmail(appUserDTO.getEmail())){
            return ResponseEntity.badRequest().body("Email already exists");
        }
        appUserService.create(appUserDTO);
        return ResponseEntity.ok().build();
    }


}
