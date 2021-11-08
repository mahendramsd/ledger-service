package com.msd.ledgerservice.service;

import com.msd.ledgerservice.domain.Ledger;
import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.repositories.LedgerRepository;
import com.msd.ledgerservice.service.impl.LedgerServiceImpl;
import com.msd.ledgerservice.util.CreditDebitStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class LedgerServiceTest {

    @InjectMocks
    LedgerServiceImpl ledgerServiceImpl;

    @Mock
    LedgerRepository ledgerRepository;

    @Test
    public void testDeposit() {

        LedgerRequest mockLedgerRequest = Mockito.mock(LedgerRequest.class);

        DepositResponse mockDepositResponse = Mockito.mock(DepositResponse.class);

        Ledger mockLedgerCredit = Mockito.mock(Ledger.class);

        Ledger mockLedgerDebit = Mockito.mock(Ledger.class);

        String accountNo = "C1000";
        String bankAccount = "B1000";
        BigDecimal amount = BigDecimal.ONE;

        BigDecimal balance = BigDecimal.ONE;

        Mockito.when(ledgerServiceImpl.getBalance(accountNo)).thenReturn(balance);

        balance = balance.add(amount);

        Mockito.when(ledgerServiceImpl.generateLedger(mockLedgerRequest,accountNo,balance, CreditDebitStatus.CREDIT)).thenReturn(mockLedgerCredit);

        BigDecimal balanceD = BigDecimal.ONE;

        Mockito.when(ledgerServiceImpl.getBalance(accountNo)).thenReturn(balance);

        balanceD = balanceD.subtract(amount);

        Mockito.when(ledgerServiceImpl.generateLedger(mockLedgerRequest,bankAccount,balanceD, CreditDebitStatus.DEBIT)).thenReturn(mockLedgerDebit);

        Assert.assertEquals(mockDepositResponse,ledgerServiceImpl.deposit(mockLedgerRequest));

    }

    @Test
    public void testWithdraw() {
        // TODO Implement withdraw method
    }

    @Test
    public void testCheckBalance() {
        // TODO Implement checkBalance method
    }

    @Test
    public void testViewHistory() {
        // TODO Implement checkBalance method
    }
}
