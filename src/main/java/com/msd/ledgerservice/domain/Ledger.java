package com.msd.ledgerservice.domain;

import com.msd.ledgerservice.util.CreditDebitStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ledger_posting")
public class Ledger extends BaseEntityMaster {

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "journal_no")
    private String journalNo;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "description")
    private String description;

    @Column(name = "credit_debit")
    @Enumerated(EnumType.STRING)
    private CreditDebitStatus creditDebitStatus;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "ledger_date")
    @Temporal(TemporalType.DATE)
    private Date ledgerDate;

    @Column(name = "balance")
    private BigDecimal balance;

}
