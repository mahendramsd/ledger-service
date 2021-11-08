package com.msd.ledgerservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class DepositResponse implements Serializable {

    private String message;
    private String customerAccount;
    private String cashBookAccount;
    private String transactionNo;
    private BigDecimal amount;
    private BigDecimal Balance;
    private Date date;
    private LocalDateTime createdDate;
}
