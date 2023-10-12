package com.loanservice.loanservice;

import com.loanservice.loanservice.entities.LoanEntity;
import com.loanservice.loanservice.exceptions.InvalidRequestException;
import com.loanservice.loanservice.models.LoanAggregation;
import com.loanservice.loanservice.repositories.LoanRepository;
import com.loanservice.loanservice.services.LoanService;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LoanServiceApplicationTests {

    @Test
    void contextLoads() {
    }


    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Test
    public void testGetAllLoans() {
        // Arrange: Mock the repository to return a list of LoanEntities
        List<LoanEntity> loanEntities = Arrays.asList(new LoanEntity(1L, 1L, 1L, 1000.0, 50.0, 20.0));
        Mockito.when(loanRepository.findAll()).thenReturn(loanEntities);

        // Act: Call the service method
        List<LoanEntity> result = loanService.getAllLoans();

        // Assert: Check if the result matches the expected list of LoanEntities
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals(1000.0, result.get(0).getAmount(), 0.01);
    }

    @Test
    public void testAddLoan() {
        // Arrange: Create a new LoanEntity
        LoanEntity newLoanEntity = new LoanEntity(2L, 2L, 2L, 1500.0, 75.0, 30.0);
        Mockito.when(loanRepository.save(newLoanEntity)).thenReturn(newLoanEntity);

        // Act: Call the service method
        newLoanEntity.setDueDate(DateUtil.tomorrow());
        newLoanEntity.setPaymentDate(new Date());
        LoanEntity result = loanService.addLoan(newLoanEntity);

        // Assert: Check if the result matches the input LoanEntity
        assertEquals(2L, result.getId().longValue());
        assertEquals(1500.0, result.getAmount(), 0.01);
    }

    @Test
    public void testAddLoanWithDatesException() {
        // Arrange: Create a new LoanEntity
        LoanEntity newLoanEntity = new LoanEntity(2L, 2L, 2L, 1500.0, 75.0, 30.0);
        Mockito.when(loanRepository.save(newLoanEntity)).thenReturn(newLoanEntity);

        // Act: Call the service method
        newLoanEntity.setDueDate(new Date());
        newLoanEntity.setPaymentDate(DateUtil.tomorrow());
        // Assert: Check if the result throws exception
        assertThrows(InvalidRequestException.class, () -> loanService.addLoan(newLoanEntity), "Payment date cannot be greater than due date");
    }

    @Test
    public void testGetLoanById() {
        // Arrange: Mock the repository to return a specific LoanEntity
        LoanEntity loanEntity = new LoanEntity(3L, 3L, 3L, 2000.0, 100.0, 40.0);
        Mockito.when(loanRepository.findById(3L)).thenReturn(Optional.of(loanEntity));

        // Act: Call the service method
        LoanEntity result = loanService.getLoanById(3L);

        // Assert: Check if the result matches the expected LoanEntity
        assertEquals(3L, result.getId().longValue());
        assertEquals(2000.0, result.getAmount(), 0.01);
    }

    @Test
    public void testGetLoansByCustomerId() {
        // Arrange: Mock the repository to return a list of LoanEntities for a specific customer
        List<LoanEntity> loanEntities = Arrays.asList(new LoanEntity(4L, 4L, 4L, 2500.0, 125.0, 50.0));
        Mockito.when(loanRepository.findByCustomerId(4L)).thenReturn(loanEntities);

        // Act: Call the service method
        List<LoanEntity> result = loanService.getLoansByCustomerId(4L);

        // Assert: Check if the result matches the expected list of LoanEntities
        assertEquals(1, result.size());
        assertEquals(4L, result.get(0).getId().longValue());
        assertEquals(2500.0, result.get(0).getAmount(), 0.01);
    }

    @Test
    public void testGetLoansByLenderId() {
        // Arrange: Mock the repository to return a list of LoanEntities for a specific lender
        List<LoanEntity> loanEntities = Arrays.asList(new LoanEntity(5L, 5L, 5L, 3000.0, 150.0, 60.0));
        Mockito.when(loanRepository.findByLenderId(5L)).thenReturn(loanEntities);

        // Act: Call the service method
        List<LoanEntity> result = loanService.getLoansByLenderId(5L);

        // Assert: Check if the result matches the expected list of LoanEntities
        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId().longValue());
        assertEquals(3000.0, result.get(0).getAmount(), 0.01);
    }

    @Test
    public void testGetAggregateLoansByLender() {
        // Arrange: Mock the repository to return a list of loans for a specific lender
        List<LoanEntity> loanEntities = Arrays.asList(
                new LoanEntity(1L, 1L, 1L, 1000.0, 50.0, 20.0),
                new LoanEntity(2L, 1L, 1L, 2000.0, 100.0, 40.0)
        );
        Mockito.when(loanRepository.findByLenderId(1L)).thenReturn(loanEntities);

        // Act: Call the service method
        LoanAggregation aggregation = loanService.getAggregateLoansByLender(1L);

        // Assert: Check if the aggregation data matches the expected values
        assertEquals(150.0, aggregation.getRemainingAmount());
        assertEquals(60.0, aggregation.getTotalInterest());
    }

    @Test
    public void testGetAggregateLoansByCustomerId() {
        // Arrange: Mock the repository to return a list of loans for a specific customer
        List<LoanEntity> loanEntities = Arrays.asList(
                new LoanEntity(3L, 2L, 1L, 1000.0, 50.0, 20.0),
                new LoanEntity(4L, 2L, 1L, 2000.0, 100.0, 40.0)
        );
        Mockito.when(loanRepository.findByCustomerId(2L)).thenReturn(loanEntities);

        // Act: Call the service method
        LoanAggregation aggregation = loanService.getAggregateLoansByCustomerId(2L);

        // Assert: Check if the aggregation data matches the expected values
        assertEquals(150.0, aggregation.getRemainingAmount());
        assertEquals(60.0, aggregation.getTotalInterest());
    }

    @Test
    public void testGetAggregateLoansByInterest() {
        // Arrange: Mock the repository to return a list of loans with a specific interest rate
        List<LoanEntity> loanEntities = Arrays.asList(
                new LoanEntity(5L, 1L, 1L, 1000.0, 50.0, 20.0),
                new LoanEntity(6L, 2L, 2L, 2000.0, 100.0, 40.0)
        );
        Mockito.when(loanRepository.findByInterestRate(20.0)).thenReturn(loanEntities);

        // Act: Call the service method
        LoanAggregation aggregation = loanService.getAggregateLoansByInterest(20.0);

        // Assert: Check if the aggregation data matches the expected values
        assertEquals(50.0, aggregation.getRemainingAmount());
        assertEquals(20.0, aggregation.getTotalInterest());
    }

}
