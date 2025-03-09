package com.hometask.dkp.hsbctransactionmanagement.service;

import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import com.hometask.dkp.hsbctransactionmanagement.exception.TransactionException;
import com.hometask.dkp.hsbctransactionmanagement.repository.TransactionMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionMemoryRepository transactionMemoryRepository;

    /**
     * 获取交易分页信息
     * @param nums
     * @param size
     * @return
     */
    @Cacheable(value = "transactionInfo", key = "#nums + '_' + #size", unless = "#result == null")
    @Override
    public Page<Transaction> getAllTransactionInfo(int nums, int size) {
        Pageable pageable = PageRequest.of(nums, size, Sort.by("id").descending());
        return transactionMemoryRepository.findAll(pageable);
    }

    /**
     * 根据id查找交易信息
     * @param id
     * @return
     */
    @Cacheable(value = "transactionInfo", key = "'_' + #id")
    public Transaction findIdByCache(Long id) {
        log.info("findIdByCache");
        return transactionMemoryRepository.findById(id).orElse(null);
    }

    /**
     * 创建交易信息
     * @param transaction
     * @return
     */
    @Override
    public Transaction createTransaction(Transaction transaction) {
        Optional<Transaction> transactionInfo = transactionMemoryRepository.findByTransactionId(transaction.getTransactionId());
        if (transactionInfo.isPresent()) {
            throw new TransactionException("The TransactionId: " + transaction.getTransactionId() + " already exists");
        }
        return transactionMemoryRepository.save(transaction);
    }

    /**
     * 提供更新交易信息
     * @param id
     * @param transaction
     * @return
     */
    @CacheEvict(value = "transactionInfo", allEntries = true)
    @Override
    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction transactionInfo = transactionMemoryRepository.findById(id).orElse(null);
        if (transactionInfo == null) {
            throw new TransactionException("The id: " + id + " does not exist");
        }
        transactionInfo.setSourceAccountId(transaction.getSourceAccountId());
        transactionInfo.setTargetAccountId(transaction.getTargetAccountId());
        transactionInfo.setAmount(transaction.getAmount());
        transactionInfo.setDescription(transaction.getDescription());
        transactionInfo.setUpdateTime(System.currentTimeMillis());

        return transactionMemoryRepository.save(transactionInfo);
    }

    /**
     * 提供删除交易信息
     * @param id
     * @return
     */
    @CacheEvict(value = "transactionInfo", allEntries = true)
    @Override
    public boolean deleteTransaction(Long id) {
        if (!transactionMemoryRepository.existsById(id)) {
            throw new TransactionException("The id: " + id + " does not exist");
        }
        transactionMemoryRepository.deleteById(id);
        return true;
    }
}
