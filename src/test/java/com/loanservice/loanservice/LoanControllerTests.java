package com.loanservice.loanservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanservice.loanservice.controllers.LoanController;
import com.loanservice.loanservice.entities.LoanEntity;
import com.loanservice.loanservice.models.LoanAggregation;
import com.loanservice.loanservice.services.LoanService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanController.class)
public class LoanControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoanController loanController;

    @MockBean
    private LoanService loanService;

    @Test
    public void testGetAllLoans() throws Exception {
        // Arrange: Mock the service to return a list of loans
        List<LoanEntity> loans = Arrays.asList(new LoanEntity(1L, 1L, 1L, 1000.0, 50.0, 20.0));
        when(loanService.getAllLoans()).thenReturn(loans);

        // Act and Assert
        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].lenderId").value(1))
                .andExpect(jsonPath("$[0].amount").value(1000.0));
    }

    @Test
    public void testAddLoan() throws Exception {
        // Arrange: Create a sample loan
        LoanEntity newLoan = new LoanEntity(2L, 2L, 2L, 1500.0, 75.0, 30.0);
        when(loanService.addLoan(any(LoanEntity.class))).thenReturn(newLoan);

        // Act and Assert
        mockMvc.perform(post("/loans/add")
                        .content(asJsonString(newLoan))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.customerId").value(2))
                .andExpect(jsonPath("$.lenderId").value(2))
                .andExpect(jsonPath("$.amount").value(1500.0));
    }

    @Test
    public void testGetLoanById() throws Exception {
        // Arrange: Mock the service to return a specific loan
        LoanEntity loan = new LoanEntity(3L, 3L, 3L, 2000.0, 100.0, 40.0);
        when(loanService.getLoanById(3L)).thenReturn(loan);

        // Act and Assert
        mockMvc.perform(get("/loans/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.customerId").value(3))
                .andExpect(jsonPath("$.lenderId").value(3))
                .andExpect(jsonPath("$.amount").value(2000.0));
    }

    @Test
    public void testGetLoansByCustomerId() throws Exception {
        // Arrange: Mock the service to return a list of loans for a specific customer
        List<LoanEntity> loans = Arrays.asList(new LoanEntity(4L, 4L, 4L, 2500.0, 125.0, 50.0));
        when(loanService.getLoansByCustomerId(4L)).thenReturn(loans);

        // Act and Assert
        mockMvc.perform(get("/loans/customer/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].customerId").value(4))
                .andExpect(jsonPath("$[0].lenderId").value(4))
                .andExpect(jsonPath("$[0].amount").value(2500.0));
    }

    @Test
    public void testGetLoansByLenderId() throws Exception {
        // Arrange: Mock the service to return a list of loans for a specific lender
        List<LoanEntity> loans = Arrays.asList(new LoanEntity(5L, 5L, 5L, 3000.0, 150.0, 60.0));
        when(loanService.getLoansByLenderId(5L)).thenReturn(loans);

        // Act and Assert
        mockMvc.perform(get("/loans/lender/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].customerId").value(5))
                .andExpect(jsonPath("$[0].lenderId").value(5))
                .andExpect(jsonPath("$[0].amount").value(3000.0));
    }

    // Helper method to convert an object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testGetAggregateLoansByLender() throws Exception {
        // Arrange: Mock the service to return a specific LoanAggregation
        LoanAggregation expectedAggregation = new LoanAggregation(3000.0, 150.0, 60.0);
        when(loanService.getAggregateLoansByLender(1L)).thenReturn(expectedAggregation);

        // Act and Assert
        mockMvc.perform(get("/loans/aggregate/lender/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingAmount").value(3000.0))
                .andExpect(jsonPath("$.totalInterest").value(150.0))
                .andExpect(jsonPath("$.totalPenalty").value(60.0));
    }

    @Test
    public void testGetAggregateLoansByCustomerId() throws Exception {
        // Arrange: Mock the service to return a specific LoanAggregation
        LoanAggregation expectedAggregation = new LoanAggregation(2000.0, 100.0, 40.0);
        when(loanService.getAggregateLoansByCustomerId(2L)).thenReturn(expectedAggregation);

        // Act and Assert
        mockMvc.perform(get("/loans/aggregate/customer/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingAmount").value(2000.0))
                .andExpect(jsonPath("$.totalInterest").value(100.0))
                .andExpect(jsonPath("$.totalPenalty").value(40.0));
    }

    @Test
    public void testGetAggregateLoansByInterest() throws Exception {
        // Arrange: Mock the service to return a specific LoanAggregation
        LoanAggregation expectedAggregation = new LoanAggregation(5000.0, 250.0, 100.0);
        when(loanService.getAggregateLoansByInterest(5.0)).thenReturn(expectedAggregation);

        // Act and Assert
        mockMvc.perform(get("/loans/aggregate/interest/5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remainingAmount").value(5000.0))
                .andExpect(jsonPath("$.totalInterest").value(250.0))
                .andExpect(jsonPath("$.totalPenalty").value(100.0));
    }

}
