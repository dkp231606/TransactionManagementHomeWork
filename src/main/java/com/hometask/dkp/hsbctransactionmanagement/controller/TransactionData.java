package com.hometask.dkp.hsbctransactionmanagement.controller;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Data
public class TransactionData {

    /**
     * 交易编号
     * 校验交易是否重复
     */
    @NotBlank(message = "The transactionId can not null")
    @Size(min = 1, max = 100, message = "The transactionId length must 1~100")
    private String transactionId;

    /**
     * 转账账户ID
     */
    @NotNull(message = "The sourceAccountId can not null")
    @Min(value = 1, message = "The sourceAccountId must positive number")
    private Long sourceAccountId;

    /**
     * 到账账户ID
     */
    @NotNull(message = "The targetAccountId can not null")
    @Min(value = 1, message = "The targetAccountId must positive number")
    private Long targetAccountId;

    /**
     * 转账金额
     * 实际需要考虑精度，需要使用BigDecimal
     */
    @NotNull(message = "The amount can not null")
    @DecimalMin(value = "0.01", inclusive = false, message = "The amount must large than 0.01")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "The amount must be to two decimal places")
    private BigDecimal amount;

    /**
     * 银行交易备注信息
     */
    @NotNull(message = "The description can not null")
    @Size(min = 1, max = 100, message = "The transactionId length must 1~100")
    private String description;
}
