package com.example.banking.controller;

import com.example.banking.controller.exception.BadRequestException;
import com.example.banking.controller.model.TransferDTO;
import com.example.banking.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(@Autowired TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferDTO> makeTransfer(
            @RequestBody @Valid TransferDTO transfer
    ) {
        if (Objects.equals(transfer.getFrom(), transfer.getTo()))
            throw new BadRequestException("Source and destination accounts are the same");

        return ResponseEntity.ok(transferService.makeTransfer(transfer));
    }
}
