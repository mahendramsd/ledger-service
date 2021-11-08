package com.msd.ledgerservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode
public class WithdrawResponse implements Serializable {

    private String message;
    private String creditAccount;
    private String debitAccount;
    private BigDecimal amount;
    private String transactionNo;
    private String description;
    private Integer branchId;
    private String currencyCode;
    private Date date;
    private LocalDateTime createdDate;
    private BigDecimal balance;
}
