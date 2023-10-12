package com.loanservice.loanservice.services;

import com.loanservice.loanservice.entities.LoanEntity;
import com.loanservice.loanservice.exceptions.InvalidRequestException;
import com.loanservice.loanservice.models.LoanAggregation;
import com.loanservice.loanservice.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<LoanEntity> getAllLoans() {
        return loanRepository.findAll();
    }

    public LoanEntity addLoan(LoanEntity loan) throws InvalidRequestException {
        validateLoanDates(loan);
        return loanRepository.save(loan);
    }

    private void validateLoanDates(LoanEntity loan) {
        if (loan.getPaymentDate().after(loan.getDueDate())) {
            throw new InvalidRequestException("Payment date cannot be greater than due date");
        }
    }

    public LoanEntity getLoanById(Long loanId) {
        return loanRepository.findById(loanId).orElse(null);
    }

    public List<LoanEntity> getLoansByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId);
    }

    public List<LoanEntity> getLoansByLenderId(Long lenderId) {
        return loanRepository.findByLenderId(lenderId);
    }

    public LoanAggregation getAggregateLoansByLender(Long lenderId) {
        List<LoanEntity> loansByLender = Optional.of(getLoansByLenderId(lenderId).stream()
                .filter(loan -> loan.getLenderId().equals(lenderId)).toList()).orElse(new ArrayList<>());

        double remainingAmount = loansByLender.stream()
                .mapToDouble(LoanEntity::getRemainingAmount)
                .sum();

        double totalInterest = loansByLender.stream()
                .mapToDouble(LoanEntity::getInterest)
                .sum();

        double totalPenalty = loansByLender.stream()
                .mapToDouble(LoanEntity::getPenalty)
                .sum();

        return new LoanAggregation(remainingAmount, totalInterest, totalPenalty);
    }

    public LoanAggregation getAggregateLoansByCustomerId(Long customerId) {
        List<LoanEntity> loansByCustomerId = Optional.of(getLoansByCustomerId(customerId).stream()
                .filter(loan -> loan.getCustomerId().equals(customerId)).toList()).orElse(new ArrayList<>());

        double remainingAmount = loansByCustomerId.stream()
                .mapToDouble(LoanEntity::getRemainingAmount)
                .sum();

        double totalInterest = loansByCustomerId.stream()
                .mapToDouble(LoanEntity::getInterest)
                .sum();

        double totalPenalty = loansByCustomerId.stream()
                .mapToDouble(LoanEntity::getPenalty)
                .sum();

        return new LoanAggregation(remainingAmount, totalInterest, totalPenalty);
    }

    public LoanAggregation getAggregateLoansByInterest(double interestRate) {
        List<LoanEntity> loansWithInterest = loanRepository.findByInterestRate(interestRate);
        loansWithInterest = Optional.of(loansWithInterest.stream()
                .filter(loan -> loan.getInterest() == interestRate)
                .collect(Collectors.toList())).orElse(new ArrayList<>());

        double remainingAmount = loansWithInterest.stream()
                .mapToDouble(LoanEntity::getRemainingAmount)
                .sum();

        double totalInterest = loansWithInterest.stream()
                .mapToDouble(LoanEntity::getInterest)
                .sum();

        double totalPenalty = loansWithInterest.stream()
                .mapToDouble(LoanEntity::getPenalty)
                .sum();

        return new LoanAggregation(remainingAmount, totalInterest, totalPenalty);
    }
}
