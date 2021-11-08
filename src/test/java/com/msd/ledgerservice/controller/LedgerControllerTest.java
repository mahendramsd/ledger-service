package com.msd.ledgerservice.controller;

import com.msd.ledgerservice.domain.Ledger;
import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.BalanceResponse;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.dto.response.TransactionHistoryResponse;
import com.msd.ledgerservice.dto.response.WithdrawResponse;
import com.msd.ledgerservice.service.LedgerService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LedgerControllerTest {

    @InjectMocks
    private LedgerController ledgerController;

    @Mock
    private LedgerService ledgerService;

    @Test
    public void depositTest() {

        LedgerRequest mockLedgerRequest = Mockito.mock(LedgerRequest.class);

        DepositResponse mockDepositResponse = new DepositResponse();
        mockDepositResponse.setBalance(BigDecimal.ONE);
        mockDepositResponse.setAmount(BigDecimal.ONE);
        mockDepositResponse.setCustomerAccount("C1000"); // Customer Account
        mockDepositResponse.setCashBookAccount("D1000");  // Bank Cashbook Account
        mockDepositResponse.setDate(new Date());
        mockDepositResponse.setCreatedDate(LocalDateTime.now());
        mockDepositResponse.setMessage("Deposit Successfully");

        Mockito.when(ledgerService.deposit(mockLedgerRequest)).thenReturn(mockDepositResponse);

        Assert.assertEquals(mockDepositResponse,ledgerController.deposit(mockLedgerRequest).getBody());
    }

    @Test
    public void withdrawTest() {

        LedgerRequest mockLedgerRequest = Mockito.mock(LedgerRequest.class);

        WithdrawResponse mockWithdrawResponse = new WithdrawResponse();
        mockWithdrawResponse.setBalance(BigDecimal.ONE);
        mockWithdrawResponse.setAmount(BigDecimal.ONE);
        mockWithdrawResponse.setCashBookAccount("D1000"); // Bank Cashbook Account
        mockWithdrawResponse.setCustomerAccount("C1000"); // Customer Account
        mockWithdrawResponse.setDate(new Date());
        mockWithdrawResponse.setCreatedDate(LocalDateTime.now());
        mockWithdrawResponse.setMessage("Deposit Successfully");

        Mockito.when(ledgerService.withdraw(mockLedgerRequest)).thenReturn(mockWithdrawResponse);

        Assert.assertEquals(mockWithdrawResponse,ledgerController.withdraw(mockLedgerRequest).getBody());
    }

    @Test
    public void checkBalanceTest() {

        String bankAccount = "C1000";

        BalanceResponse balanceResponse = new BalanceResponse(bankAccount,BigDecimal.ONE);

        Mockito.when(ledgerService.checkBalance(bankAccount)).thenReturn(balanceResponse);

        Assert.assertEquals(balanceResponse,ledgerController.checkBalance(bankAccount).getBody());
    }

    @Test
    public void transactionHistoryTest() throws ParseException {

        String bankAccount = "C1000";
        String fromDate = "2021-11-07";
        String toDate = "2021-11-08";

        List<TransactionHistoryResponse> historyResponseList = new ArrayList<>();
        Ledger ledger = new Ledger();
        ledger.setAccountNo("C1000");
        ledger.setBranchId(1);
        ledger.setCurrencyCode("LKR");
        ledger.setJournalNo("J001");
        ledger.setReferenceNo("TR-0001");
        ledger.setDescription("Sample Deposit");
        ledger.setStatus(Status.ACTIVE);
        TransactionHistoryResponse historyResponse =  new TransactionHistoryResponse(ledger);
        historyResponseList.add(historyResponse);

        Mockito.when(ledgerService.viewHistory(bankAccount,fromDate,toDate)).thenReturn(historyResponseList);

        Assert.assertEquals(historyResponseList,ledgerController.transactionHistory(bankAccount,fromDate,toDate).getBody());
    }
}
