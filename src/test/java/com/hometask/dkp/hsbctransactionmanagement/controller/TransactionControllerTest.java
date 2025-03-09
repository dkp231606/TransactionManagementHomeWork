package com.hometask.dkp.hsbctransactionmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import com.hometask.dkp.hsbctransactionmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateTransaction() throws Exception {
        String transactionId = "transactionIdCreateTest";
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId(transactionId);
        transaction.setAmount(BigDecimal.valueOf(220.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        String transactionCreateJson = objectMapper.writeValueAsString(transaction);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionCreateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.transactionId").value(transactionId))
                .andExpect(jsonPath("$.data.amount").value(220.0))
                .andExpect(jsonPath("$.data.sourceAccountId").value(1001))
                .andExpect(jsonPath("$.data.targetAccountId").value(1002L))
                .andDo(print());
    }

    @Test
    public void testGetTransaction() throws Exception {
        String transactionId = "transactionIdGetTest";
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId(transactionId);
        transaction.setAmount(BigDecimal.valueOf(220.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        String transactionJson = objectMapper.writeValueAsString(transaction);

        when(transactionService.findIdByCache(any(Long.class))).thenReturn(transaction);

        mockMvc.perform(get("/api/transaction/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.transactionId").value(transactionId))
                .andExpect(jsonPath("$.data.amount").value(220.0))
                .andExpect(jsonPath("$.data.sourceAccountId").value(1001))
                .andExpect(jsonPath("$.data.targetAccountId").value(1002L))
                .andDo(print());
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        String transactionId = "transactionIdUpdateTest";
        Transaction transactionUpdate = new Transaction();
        transactionUpdate.setId(1L);
        transactionUpdate.setTransactionId(transactionId);
        transactionUpdate.setDescription("Update Transaction");
        transactionUpdate.setAmount(new BigDecimal("30.00"));
        transactionUpdate.setSourceAccountId(1111L);
        transactionUpdate.setTargetAccountId(2222L);

        when(transactionService.updateTransaction(any(Long.class), any(Transaction.class))).thenReturn(transactionUpdate);
        mockMvc.perform(put("/api/transaction/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.transactionId").value(transactionId));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        when(transactionService.deleteTransaction(any(Long.class))).thenReturn(true);

        mockMvc.perform(delete("/api/transaction/{id}", 10L))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionList() throws Exception {
        String transactionId = "transactionIdGetListTest";
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId(transactionId);
        transaction.setDescription("Get All list Transaction");
        transaction.setAmount(new BigDecimal("30.00"));
        transaction.setSourceAccountId(1111L);
        transaction.setTargetAccountId(2222L);
        Page<Transaction> pageTransaction = new PageImpl<>(Collections.singletonList(transaction));

        when(transactionService.getAllTransactionInfo(any(int.class), any(int.class))).thenReturn((pageTransaction));

        mockMvc.perform(get("/api/transaction")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].transactionId").value(transactionId));
    }
}