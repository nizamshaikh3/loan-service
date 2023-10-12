package com.loanservice.loanservice.models;

import lombok.Data;

import java.util.Date;

@Data
public class Loan {
    private Long id;
    private Long customerId;
    private Long lenderId;
    private double amount;
    private double remainingAmount;
    private double interest;
    private double penalty;
    private Date paymentDate;
    private Date dueDate;
}
