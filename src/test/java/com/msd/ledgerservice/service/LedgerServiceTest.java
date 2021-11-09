package com.msd.ledgerservice.service;

import com.msd.ledgerservice.domain.Ledger;
import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.BalanceResponse;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.dto.response.TransactionHistoryResponse;
import com.msd.ledgerservice.dto.response.WithdrawResponse;
import com.msd.ledgerservice.repositories.LedgerRepository;
import com.msd.ledgerservice.service.impl.LedgerServiceImpl;
import com.msd.ledgerservice.util.CreditDebitStatus;
import com.msd.ledgerservice.util.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        Mockito.when(ledgerServiceImpl.getBalance(accountNo)).thenReturn(balanceD);

        balanceD = balanceD.subtract(amount);

        Mockito.when(ledgerServiceImpl.generateLedger(mockLedgerRequest,bankAccount,balanceD, CreditDebitStatus.DEBIT)).thenReturn(mockLedgerDebit);

        Assert.assertEquals(mockDepositResponse,ledgerServiceImpl.deposit(mockLedgerRequest));

    }

    @Test
    public void testWithdraw() {
        LedgerRequest mockLedgerRequest = Mockito.mock(LedgerRequest.class);

        WithdrawResponse mockWithdrawResponse = Mockito.mock(WithdrawResponse.class);

        Ledger mockLedgerCredit = Mockito.mock(Ledger.class);

        Ledger mockLedgerDebit = Mockito.mock(Ledger.class);

        String customerAccountNo = "C1000";
        String bankAccount = "B1000";
        BigDecimal amount = BigDecimal.ONE;

        BigDecimal balance = BigDecimal.ONE;

        Mockito.when(ledgerServiceImpl.getBalance(bankAccount)).thenReturn(balance);

        balance = balance.add(amount);

        Mockito.when(ledgerServiceImpl.generateLedger(mockLedgerRequest,bankAccount,balance, CreditDebitStatus.CREDIT)).thenReturn(mockLedgerCredit);

        BigDecimal balanceD = BigDecimal.ONE;

        Mockito.when(ledgerServiceImpl.getBalance(customerAccountNo)).thenReturn(balanceD);

        balanceD = balanceD.subtract(amount);

        Mockito.when(ledgerServiceImpl.generateLedger(mockLedgerRequest,customerAccountNo,balanceD, CreditDebitStatus.DEBIT)).thenReturn(mockLedgerDebit);

        Assert.assertEquals(mockWithdrawResponse,ledgerServiceImpl.withdraw(mockLedgerRequest));

    }

    @Test
    public void testCheckBalance() {

        BalanceResponse mockBalanceResponse = Mockito.mock(BalanceResponse.class);

        String customerAccountNo = "C1000";
        BigDecimal balance = BigDecimal.ONE;

        Mockito.when(ledgerServiceImpl.getBalance(customerAccountNo)).thenReturn(balance);

        Assert.assertEquals(mockBalanceResponse,ledgerServiceImpl.checkBalance(customerAccountNo));
    }

    @Test
    public void testGetBalance() {
        BigDecimal creditSum = BigDecimal.ONE;
        BigDecimal debitSum = BigDecimal.ZERO;
        String customerAccountNo = "C1000";

        Mockito.when(ledgerRepository.findLedgerSum(customerAccountNo,CreditDebitStatus.CREDIT, Status.ACTIVE)).thenReturn(creditSum);

        Mockito.when(ledgerRepository.findLedgerSum(customerAccountNo,CreditDebitStatus.DEBIT, Status.ACTIVE)).thenReturn(debitSum);

        BigDecimal balance = creditSum.subtract(debitSum);

        Assert.assertEquals(balance,ledgerServiceImpl.getBalance(customerAccountNo));
    }

    @Test
    public void testViewHistory() throws ParseException {
        TransactionHistoryResponse mockTransactionHistoryResponse = Mockito.mock(TransactionHistoryResponse.class);
        String customerAccountNo = "C1000";
        String fromDate = "2021-11-09";
        String toDate = "2021-11-10";

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse(fromDate);
        Date date2 = simpleDateFormat.parse(toDate);

        List<Ledger> ledgers = new ArrayList<>();
        Ledger mockLedger = Mockito.mock(Ledger.class);
        ledgers.add(mockLedger);

        Mockito.when(ledgerRepository.findByAccountNoAndLedgerDateBetween(customerAccountNo,date1, date2)).thenReturn(ledgers);

        Assert.assertEquals(mockTransactionHistoryResponse,ledgerServiceImpl.viewHistory(customerAccountNo,fromDate,toDate));
    }
}
