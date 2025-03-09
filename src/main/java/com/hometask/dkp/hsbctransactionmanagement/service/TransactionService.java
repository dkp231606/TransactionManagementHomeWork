package com.hometask.dkp.hsbctransactionmanagement.service;

import com.hometask.dkp.hsbctransactionmanagement.controller.TransactionData;
import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import org.springframework.data.domain.Page;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

public interface TransactionService {
    /**
     * 在系统中新增交易信息
     * @param transaction
     * @return
     */
    Transaction createTransaction(TransactionData transactionData);

    /**
     * 修改事件
     * @param id
     * @param transaction
     * @return
     */
    Transaction updateTransaction(Long id, Transaction transaction);

    /**
     * 删除事件
     * @param id
     */
    boolean deleteTransaction(Long id);

    /**
     * 在缓存中，根据id判断是否存在
     * @param id
     * @return
     */
    Transaction findIdByCache(Long id);

    /**
     * 获取所有交易信息，支持分页查询
     * @param nums
     * @param size
     * @return
     */
    Page<Transaction> getAllTransactionInfo(int nums, int size);
}
