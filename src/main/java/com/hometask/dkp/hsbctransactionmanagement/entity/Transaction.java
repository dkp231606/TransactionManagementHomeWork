package com.hometask.dkp.hsbctransactionmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Data
@Entity
@Table(indexes = {@Index(name = "idx_transactionId", columnList = "transactionId", unique = true)})
public class Transaction {

    /**
     * 系统主键id
     * 全局唯一，使用数据库自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易编号
     * 校验交易是否重复
     */
    private String transactionId;

    /**
     * 转账账户ID
     */
    private Long sourceAccountId;

    /**
     * 到账账户ID
     */
    private Long targetAccountId;

    /**
     * 转账金额
     * 实际需要考虑精度，需要使用BigDecimal
     */
    private BigDecimal amount;

    /**
     * 银行交易备注信息
     */
    private String description;

    /**
     * 交易创建时间
     */
    private long createTime;

    /**
     * 交易更新时间
     */
    private long updateTime;
}
