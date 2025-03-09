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
//    @NotBlank(message = "The transactionId can not null")
//    @Size(min = 1, max = 100, message = "The transactionId length must 1~100")
    private String transactionId;

    /**
     * 转账账户ID
     */
//    @NotNull(message = "The sourceAccountId can not null")
    private Long sourceAccountId;

    /**
     * 到账账户ID
     */
//    @NotNull(message = "The targetAccountId can not null")
    private Long targetAccountId;

    /**
     * 转账金额
     * 实际需要考虑精度，需要使用BigDecimal
     */
//    @NotNull(message = "The amount can not null")
//    @DecimalMin(value = "0.01", inclusive = false, message = "The amount must large than 0.01")
//    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "金额必须为两位小数")
    private BigDecimal amount;

    /**
     * 银行交易备注信息
     */
//    @NotNull(message = "The description can not null")
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
