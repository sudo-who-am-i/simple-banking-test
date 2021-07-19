package com.example.banking.repository.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Transfer {

    @Id
    @GeneratedValue(generator = "transfer_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private ZonedDateTime transferDate;

    @OneToOne
    private Account from;

    @OneToOne
    private Account to;
}
