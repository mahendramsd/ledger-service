package com.msd.ledgerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse implements Serializable {

    private String accountNo;
    private BigDecimal balance;
}
