package com.loanservice.loanservice.controllers;

import com.loanservice.loanservice.entities.LoanEntity;
import com.loanservice.loanservice.exceptions.LoanNotFoundException;
import com.loanservice.loanservice.models.LoanAggregation;
import com.loanservice.loanservice.services.LoanService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@GraphQLApi
public class LoanGraphQLController {

    private final LoanService loanService;

    @Autowired
    public LoanGraphQLController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GraphQLQuery
    public List<LoanEntity> getAllLoans() {
        List<LoanEntity> loans = loanService.getAllLoans();
        return loans;
    }

    @GraphQLMutation
    public LoanEntity addLoan(@GraphQLArgument LoanEntity loan) {
        LoanEntity addedLoan;
        addedLoan = loanService.addLoan(loan);
        return addedLoan;
    }

    @GraphQLQuery
    public LoanEntity getLoanById(@GraphQLArgument Long loanId) {
        LoanEntity loan = loanService.getLoanById(loanId);
        if (loan != null) {
            return loan;
        } else {
            throw new LoanNotFoundException("Loan with ID " + loanId + " not found.");
        }
    }

    @GraphQLQuery
    public List<LoanEntity> getLoansByCustomerId(@GraphQLArgument Long customerId) {
        List<LoanEntity> loans = loanService.getLoansByCustomerId(customerId);
        return loans;
    }

    @GraphQLQuery
    public List<LoanEntity> getLoansByLenderId(@GraphQLArgument Long lenderId) {
        List<LoanEntity> loans = loanService.getLoansByLenderId(lenderId);
        return loans;
    }

    @GraphQLQuery
    public LoanAggregation getAggregateLoansByLender(@GraphQLArgument Long lenderId) {
        LoanAggregation aggregation = loanService.getAggregateLoansByLender(lenderId);
        return aggregation;
    }

    @GraphQLQuery
    public LoanAggregation getAggregateLoansByCustomerId(@GraphQLArgument Long customerId) {
        LoanAggregation aggregation = loanService.getAggregateLoansByCustomerId(customerId);
        return aggregation;
    }

    @GraphQLQuery
    public LoanAggregation getAggregateLoansByInterest(@GraphQLArgument double interestRate) {
        LoanAggregation aggregation = loanService.getAggregateLoansByInterest(interestRate);
        return aggregation;
    }

}
