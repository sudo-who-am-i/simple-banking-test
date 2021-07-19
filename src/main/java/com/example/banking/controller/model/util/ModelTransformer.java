package com.example.banking.controller.model.util;

import com.example.banking.controller.model.AccountDTO;
import com.example.banking.controller.model.TransferDTO;
import com.example.banking.repository.model.Account;
import com.example.banking.repository.model.Transfer;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModelTransformer {

    public static AccountDTO toDtoWithTransfers(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getBalance(),
                account.getTransfers() == null
                        ? new ArrayList<>()
                        : account.getTransfers() .stream()
                        .map(ModelTransformer::toDtoWithTransfers)
                        .collect(Collectors.toList())
        );
    }

    public static TransferDTO toDtoWithTransfers(Transfer transfer) {
        return new TransferDTO(
                transfer.getId(),
                transfer.getAmount(),
                transfer.getTransferDate(),
                transfer.getFrom().getId(),
                transfer.getTo().getId()
        );
    }

    public static Account fromDto(AccountDTO account) {
        return new Account(
                account.getId(),
                account.getBalance()
        );
    }

    public static Transfer fromDto(TransferDTO transfer, Account from, Account to) {
        return new Transfer(
                transfer.getId(),
                transfer.getAmount(),
                transfer.getTransferDate(),
                from,
                to
        );
    }
}
