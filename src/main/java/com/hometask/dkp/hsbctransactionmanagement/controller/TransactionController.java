package com.hometask.dkp.hsbctransactionmanagement.controller;

import com.hometask.dkp.hsbctransactionmanagement.entity.Transaction;
import com.hometask.dkp.hsbctransactionmanagement.exception.TransactionException;
import com.hometask.dkp.hsbctransactionmanagement.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@RestController
@RequestMapping("/api/transaction")
@Validated
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * 获取交易分页查询结果
     * @param nums
     * @param size
     * @return
     */
    @GetMapping
    public ResponseResult<Page<Transaction>> getTransactionList(@RequestParam(value = "nums", required = false, defaultValue = "0") int nums,
                                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Page<Transaction> pageRes = transactionService.getAllTransactionInfo(nums, size);
        return ResponseResult.succeed(pageRes);
    }

    /**
     * 根据id获取交易信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult<Transaction> getTransaction(@PathVariable(value = "id") Long id) {
        Transaction transaction = transactionService.findIdByCache(id);
        return ResponseResult.succeed(transaction);
    }

    /**
     * 创建交易信息
     * @param transaction
     * @return
     */
    @PostMapping
    public ResponseResult<?> createTransaction(@RequestBody @Valid Transaction transaction) {
        Transaction result = new Transaction();
        result.setTransactionId(transaction.getTransactionId());
        result.setSourceAccountId(transaction.getSourceAccountId());
        result.setTargetAccountId(transaction.getTargetAccountId());
        result.setAmount(transaction.getAmount());
        result.setDescription(transaction.getDescription());
        result.setCreateTime(System.currentTimeMillis());
        result.setUpdateTime(System.currentTimeMillis());

        try {
            Transaction transaction2 = transactionService.createTransaction(transaction);

            return ResponseResult.succeed(transaction2);
        } catch (TransactionException e) {
            return ResponseResult.fail(StatusEnums.FAIL.getCode(), e.getMessage());
        }
    }

    /**
     * 修改交易信息
     * @param id
     * @param transaction
     * @return
     */
    @PutMapping("/{id}")
    public ResponseResult<Transaction> updateTransaction(@PathVariable(value = "id") Long id, @RequestBody @Valid Transaction transaction) {
        Transaction result = new Transaction();
        result.setSourceAccountId(transaction.getSourceAccountId());
        result.setTargetAccountId(transaction.getTargetAccountId());
        result.setAmount(transaction.getAmount());
        result.setDescription(transaction.getDescription());
        result.setCreateTime(System.currentTimeMillis());
        try {
            return ResponseResult.succeed(transactionService.updateTransaction(id, transaction));
        } catch (TransactionException e) {
            return ResponseResult.fail(StatusEnums.FAIL.getCode(), e.getMessage());
        }
    }

    /**
     * 删除交易信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTransaction(@PathVariable(value = "id") Long id) {
        try {
            transactionService.deleteTransaction(id);
        } catch (TransactionException e) {
            return ResponseResult.fail(StatusEnums.FAIL.getCode(), e.getMessage());
        }
        return ResponseResult.succeed();
    }
}
