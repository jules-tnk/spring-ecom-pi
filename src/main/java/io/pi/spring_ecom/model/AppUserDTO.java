package io.pi.spring_ecom.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    private Integer phoneNumber;

    @NotNull
    @Size(max = 255)
    private String password;

}
