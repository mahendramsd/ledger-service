package com.msd.ledgerservice.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class LedgerRequest implements Serializable {

    private String creditAccount; // Customer Account
    private String debitAccount; // Bank CashBook Account
    private BigDecimal amount;
    private String transactionNo;
    private String journalNo;
    private String description;
    private Integer branchId;
    private String currencyCode;
    private Date ledgerDate;
}
