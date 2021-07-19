package com.example.banking.service;

import com.example.banking.controller.exception.BadRequestException;
import com.example.banking.controller.model.TransferDTO;
import com.example.banking.controller.model.util.ModelTransformer;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransferRepository;
import com.example.banking.repository.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(@Autowired TransferRepository transferRepository,
                           @Autowired AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransferDTO> getAccountTransfers(final Long accountId) {
        var account = getAccount(accountId, String.format("Account with id %d not found", accountId));
        var transfers =  transferRepository
                .getAllByFromOrToOrderByTransferDateDesc(account, account);

        return transfers.stream()
                .map(ModelTransformer::toDtoWithTransfers)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = { SQLException.class })
    public TransferDTO makeTransfer(final TransferDTO transfer) {
        var source =  getAndVerifySource(transfer);
        var dest =  getAccount(transfer.getTo(), "Source account does not exist");
        var newTransfer = saveTransfer(transfer, source, dest);

        updateAccountsBalance(newTransfer, source, dest);
        return newTransfer;
    }

    private Account getAndVerifySource(final TransferDTO transfer) {
        var source =  accountRepository.getAccountById(transfer.getFrom())
                .orElseThrow(() -> new BadRequestException("Source account does not exist"));
        if (source.getBalance().doubleValue() < transfer.getAmount().doubleValue()) {
            throw new BadRequestException("Source account balance is not enough");
        }
        return source;
    }

    private Account getAccount(final Long accountId,
                               final String fallbackMessage) {
        return accountRepository.getAccountById(accountId)
                .orElseThrow(() -> new BadRequestException(fallbackMessage));
    }

    private TransferDTO saveTransfer(final TransferDTO transfer,
                                     final Account source,
                                     final Account dest) {
        var domainModel = ModelTransformer.fromDto(transfer, source, dest);
        return ModelTransformer.toDtoWithTransfers(transferRepository.save(domainModel));
    }

    private void updateAccountsBalance(final TransferDTO transfer,
                                       final Account source,
                                       final Account dest) {
        source.setBalance(source.getBalance().subtract(transfer.getAmount()));
        accountRepository.save(source);

        dest.setBalance(dest.getBalance().add(transfer.getAmount()));
        accountRepository.save(dest);
    }
}
