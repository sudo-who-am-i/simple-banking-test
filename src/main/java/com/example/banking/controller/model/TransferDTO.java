package com.example.banking.controller.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferDTO {

    private Long id;

    @NotNull(message = "Transfer amount must not be empty")
    @Positive(message = "Transfer amount must be higher than 0")
    private BigDecimal amount;

    private ZonedDateTime transferDate = ZonedDateTime.now();

    @NotNull(message = "Transfer source account is not specified")
    private Long from;

    @NotNull(message = "Transfer destination account is not specified")
    private Long to;
}
