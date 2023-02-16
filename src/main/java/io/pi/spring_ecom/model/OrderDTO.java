package io.pi.spring_ecom.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer quantity;

    @NotNull
    private Long client;

    @NotNull
    private Long product;

    private Double totalPrice;

}
