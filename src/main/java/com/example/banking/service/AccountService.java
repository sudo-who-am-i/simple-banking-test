package com.example.banking.service;

import com.example.banking.controller.exception.BadRequestException;
import com.example.banking.controller.model.AccountDTO;
import com.example.banking.controller.model.util.ModelTransformer;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransferRepository;
import com.example.banking.repository.model.Account;
import com.example.banking.repository.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public AccountService(@Autowired AccountRepository accountRepository,
                          @Autowired TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    public Optional<AccountDTO> getAccount(final Long id) {
        var account =  accountRepository.getAccountById(id);
        if (account.isEmpty()) {
            throw new BadRequestException(String.format("Account with id %d does not exist", id));
        }

        Function<Account, List<Transfer>> transfers = (a) ->
            transferRepository.getAllByFromOrToOrderByTransferDateDesc(a, a);

        return account
                .map(a -> a.withTransfers(transfers.apply(a)))
                .map(ModelTransformer::toDtoWithTransfers);
    }

    public AccountDTO create(final AccountDTO account) {
        if (account.getId() != null
                && accountRepository.getAccountById(account.getId()).isPresent()) {
            throw new BadRequestException("The account is already exists");
        }

        var domainModel = ModelTransformer.fromDto(account);
        return ModelTransformer.toDtoWithTransfers(accountRepository.save(domainModel));
    }
}
