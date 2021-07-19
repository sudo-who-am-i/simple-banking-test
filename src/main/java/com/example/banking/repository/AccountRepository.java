package com.example.banking.repository;

import com.example.banking.repository.model.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> getAccountById(Long id);

    @Override
    @NotNull Account save(@NotNull Account account);
}
