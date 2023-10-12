package com.loanservice.loanservice.repositories;

import com.loanservice.loanservice.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    @Query(value = "Select * from loan where customer_id = ?1", nativeQuery = true)
    List<LoanEntity> findByCustomerId(Long customerId);

    @Query(value = "Select * from loan where lender_id = ?1", nativeQuery = true)
    List<LoanEntity> findByLenderId(Long lenderId);

    @Query(value = "Select * from loan where interest_rate = ?1", nativeQuery = true)
    List<LoanEntity> findByInterestRate(double interestRate);
}
