package com.msd.ledgerservice.dto.response;

import com.msd.ledgerservice.domain.Ledger;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode
public class TransactionHistoryResponse implements Serializable {

    private Date ledgerDate;
    private BigDecimal amount;
    private String transactionNo;
    private String journalNo;
    private String description;
    private String status;
    private String creditDebitStatus;
    private LocalDateTime createdDate;
    private BigDecimal balance;

    public TransactionHistoryResponse(Ledger ledger) {
        this.ledgerDate = ledger.getLedgerDate();
        this.amount = ledger.getAmount();
        this.transactionNo = ledger.getReferenceNo();
        this.journalNo = ledger.getJournalNo();
        this.description = ledger.getDescription();
        this.status = ledger.getStatus().name();
        this.creditDebitStatus = ledger.getCreditDebitStatus().name();
        this.createdDate = ledger.getCreatedDate();
        this.balance = ledger.getBalance();
    }
}
