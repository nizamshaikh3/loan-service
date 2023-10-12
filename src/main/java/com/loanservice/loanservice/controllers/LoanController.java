package com.loanservice.loanservice.controllers;

import com.loanservice.loanservice.entities.LoanEntity;
import com.loanservice.loanservice.exceptions.LoanNotFoundException;
import com.loanservice.loanservice.models.LoanAggregation;
import com.loanservice.loanservice.services.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@Slf4j
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping()
    public ResponseEntity<List<LoanEntity>> getAllLoans() {
        List<LoanEntity> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/add")
    public ResponseEntity<LoanEntity> addLoan(@RequestBody LoanEntity loan) {
        LoanEntity addedLoan;
        addedLoan = loanService.addLoan(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedLoan);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanEntity> getLoanById(@PathVariable Long loanId) {
        LoanEntity loan = loanService.getLoanById(loanId);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        } else {
            throw new LoanNotFoundException("Loan with ID " + loanId + " not found.");
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanEntity>> getLoansByCustomerId(@PathVariable Long customerId) {
        List<LoanEntity> loans = loanService.getLoansByCustomerId(customerId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<List<LoanEntity>> getLoansByLenderId(@PathVariable Long lenderId) {
        List<LoanEntity> loans = loanService.getLoansByLenderId(lenderId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/aggregate/lender/{lenderId}")
    public ResponseEntity<LoanAggregation> getAggregateLoansByLender(@PathVariable Long lenderId) {
        LoanAggregation aggregation = loanService.getAggregateLoansByLender(lenderId);
        return ResponseEntity.ok(aggregation);
    }

    @GetMapping("/aggregate/customer/{customerId}")
    public ResponseEntity<LoanAggregation> getAggregateLoansByCustomerId(@PathVariable Long customerId) {
        LoanAggregation aggregation = loanService.getAggregateLoansByCustomerId(customerId);
        return ResponseEntity.ok(aggregation);
    }

    @GetMapping("/aggregate/interest/{interestRate}")
    public ResponseEntity<LoanAggregation> getAggregateLoansByInterest(@PathVariable double interestRate) {
        LoanAggregation aggregation = loanService.getAggregateLoansByInterest(interestRate);
        return ResponseEntity.ok(aggregation);
    }

}
