package com.example.banking.controller.model;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {

    private Long id;

    @NotNull(message = "Account balance should not be empty")
    @Min(value = 0, message = "Balance value must be higher than 0")
    private BigDecimal balance;

    private List<TransferDTO> transfers;
}
