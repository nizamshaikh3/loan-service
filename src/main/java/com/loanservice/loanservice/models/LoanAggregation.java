package com.loanservice.loanservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanAggregation {
    private double remainingAmount;
    private double totalInterest;
    private double totalPenalty;
}
