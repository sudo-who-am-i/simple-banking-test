package com.example.banking.repository;

import com.example.banking.repository.model.Account;
import com.example.banking.repository.model.Transfer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransferRepository extends CrudRepository<Transfer, Long> {

    List<Transfer> getAllByFromOrToOrderByTransferDateDesc(Account from, Account to);

    @Override
    @NotNull Transfer save(@NotNull Transfer transfer);
}
