package com.example.banking.repository.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "account_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private BigDecimal balance;

    @Transient
    private List<Transfer> transfers;

    public Account(Long id, @NonNull BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account withTransfers(List<Transfer> transfers) {
        this.transfers = transfers;
        return this;
    }
}
