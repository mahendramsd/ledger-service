package com.msd.ledgerservice.repositories;

import com.msd.ledgerservice.domain.Ledger;
import com.msd.ledgerservice.util.CreditDebitStatus;
import com.msd.ledgerservice.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger,Long> {

    @Query(value = "select SUM (l.amount) from Ledger l where l.accountNo = ?1 and l.creditDebitStatus = ?2 and l.status = ?3")
    BigDecimal findLedgerSum(String accountNo, CreditDebitStatus creditDebitStatus, Status status);

    List<Ledger> findByAccountNoAndLedgerDateBetween(String accountNo, Date from, Date to);
}
