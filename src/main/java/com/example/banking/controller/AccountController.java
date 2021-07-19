package com.example.banking.controller;

import com.example.banking.controller.model.AccountDTO;
import com.example.banking.controller.model.TransferDTO;
import com.example.banking.service.AccountService;
import com.example.banking.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransferService transferService;

    public AccountController(@Autowired AccountService accountService,
                             @Autowired TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<AccountDTO>> getAccount(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> create(
            @RequestBody @Valid AccountDTO account
    ) {
        return ResponseEntity.ok(accountService.create(account));
    }

    @GetMapping("/{id}/transfers")
    public ResponseEntity<List<TransferDTO>> getTransferList(
            @PathVariable("id") Long accountId
    ) {
        return ResponseEntity.ok(transferService.getAccountTransfers(accountId));
    }

}
