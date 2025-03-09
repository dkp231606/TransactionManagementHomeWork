package com.hometask.dkp.hsbctransactionmanagement.repository;

import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Repository
public interface TransactionMemoryRepository extends JpaRepository<Transaction, Long> {

    /**
     * 根据交易编号查找交易记录
     * @param transactionId 交易编号
     * @return 包含匹配交易编号的交易记录的Optional对象，如果不存在则返回Optional.empty()
     */
    Optional<Transaction> findByTransactionId(String transactionId);
}
