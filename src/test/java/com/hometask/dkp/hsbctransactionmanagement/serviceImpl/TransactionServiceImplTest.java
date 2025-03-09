package com.hometask.dkp.hsbctransactionmanagement.serviceImpl;

import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import com.hometask.dkp.hsbctransactionmanagement.exception.TransactionException;
import com.hometask.dkp.hsbctransactionmanagement.repository.TransactionMemoryRepository;
import com.hometask.dkp.hsbctransactionmanagement.service.TransactionService;
import com.hometask.dkp.hsbctransactionmanagement.service.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
public class TransactionServiceImplTest {

    @Autowired
    private CacheManager cacheManager;

    @Mock
    private TransactionMemoryRepository transactionMemoryRepository;

    @InjectMocks
    @Spy
    private TransactionServiceImpl transactionService;


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindIdNotCache() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTransactionId("test1");
        transaction.setAmount(BigDecimal.valueOf(101.0));
        transaction.setDescription("description");
        when(transactionMemoryRepository.findById(id)).thenReturn(Optional.ofNullable(transaction));
        Transaction result = transactionService.findIdByCache(id);
        assertNotNull(result);
        assertEquals(transaction, result);
    }

    @Test
    public void testFindIdByCache() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTransactionId("test1");
        transaction.setAmount(BigDecimal.valueOf(101.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);
        when(transactionMemoryRepository.findById(id)).thenReturn(Optional.ofNullable(transaction));
        Transaction result = transactionService.findIdByCache(id);
        assertNotNull(result);
        verify(transactionService, times(1)).findIdByCache(id);

        Transaction result1 = transactionService.findIdByCache(id);
        assertNotNull(result1);
        verify(transactionService, times(2)).findIdByCache(id);
    }


    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("test1");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setTransactionId("test1");
        savedTransaction.setAmount(BigDecimal.valueOf(100.0));
        savedTransaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        when(transactionMemoryRepository.save(transaction)).thenReturn(savedTransaction);

        Transaction result = transactionService.createTransaction(transaction);
        assertEquals(savedTransaction, result);
        verify(transactionMemoryRepository, times(1)).save(transaction);
    }

    @Test
    public void testCreateTransactionForTransactionIdExists() {
        String transactionId = "transactionIdTest";
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(BigDecimal.valueOf(220.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        when(transactionMemoryRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(new Transaction()));

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.createTransaction(transaction));
        assertEquals(exception.getMessage(), "The TransactionId: " + transactionId + " already exists");
        verify(transactionMemoryRepository, times(1)).findByTransactionId(transactionId);
        verify(transactionMemoryRepository, times(0)).save(transaction);

    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        String transactionId = "updateTest";

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTransactionId(transactionId);
        transaction.setAmount(BigDecimal.valueOf(305.2));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        Transaction updateTransaction = new Transaction();
        updateTransaction.setId(id);
        updateTransaction.setTransactionId(transactionId);
        updateTransaction.setAmount(BigDecimal.valueOf(200.01));
        updateTransaction.setDescription("descriptionTestAfterUpdate");
        updateTransaction.setSourceAccountId(2001L);
        updateTransaction.setTargetAccountId(2002L);
        updateTransaction.setUpdateTime(System.currentTimeMillis());

        when(transactionMemoryRepository.findById(id)).thenReturn(Optional.of(transaction));
        when(transactionMemoryRepository.save(transaction)).thenReturn(transaction);
        Transaction result = transactionService.updateTransaction(id, updateTransaction);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(200.01), transaction.getAmount());
        assertEquals("descriptionTestAfterUpdate", transaction.getDescription());
        verify(transactionMemoryRepository, times(1)).save(transaction);
    }

    @Test
    public void testUpdateTransactionIdNotExists() {
        Long id = 1L;
        String transactionId = "updateTestFail";
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(BigDecimal.valueOf(220.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        when(transactionMemoryRepository.findById(id)).thenReturn(Optional.empty());

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.updateTransaction(id, transaction));
        assertEquals(exception.getMessage(), "The id: " + id + " does not exist");
        verify(transactionMemoryRepository, times(1)).findById(id);
        verify(transactionMemoryRepository, times(0)).save(transaction);
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;
        when(transactionMemoryRepository.existsById(id)).thenReturn(true);
        transactionService.deleteTransaction(id);
        verify(transactionMemoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteTransactionNotExist() {
        // Arrange
        Long id = 1L;
        when(transactionMemoryRepository.existsById(id)).thenReturn(false);

        TransactionException exception = assertThrows(TransactionException.class, () -> {
            transactionService.deleteTransaction(id);
        });

        assertEquals(exception.getMessage(), "The id: " + id + " does not exist");
        verify(transactionMemoryRepository, times(1)).existsById(id);
        verify(transactionMemoryRepository, times(0)).deleteById(id);
    }

    @Test
    public void TestGetAllTransactionInfo() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("id").descending());
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setTransactionId("test1");
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setDescription("description");
        transaction.setSourceAccountId(1001L);
        transaction.setTargetAccountId(1002L);

        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(transactionMemoryRepository.findAll(pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.getAllTransactionInfo( pageable.getPageNumber(), page.getSize());
        assertEquals(1, result.getTotalElements());
        verify(transactionMemoryRepository).findAll(pageable);
    }
}
