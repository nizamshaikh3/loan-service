package com.loanservice.loanservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long lenderId;
    private double amount;
    private double remainingAmount;
    private double interest;
    private double penalty;
    private Date paymentDate;
    private Date dueDate;

    public LoanEntity(Long id, Long customerId, Long lenderId, double amount, double remainingAmount, double interest) {
        this.id = id;
        this.customerId = customerId;
        this.lenderId = lenderId;
        this.amount = amount;
        this.remainingAmount = remainingAmount;
        this.interest = interest;
    }
}
