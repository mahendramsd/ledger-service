package com.msd.ledgerservice.service;

import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.BalanceResponse;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.dto.response.TransactionHistoryResponse;
import com.msd.ledgerservice.dto.response.WithdrawResponse;

import java.text.ParseException;
import java.util.List;

public interface LedgerService {

    DepositResponse deposit(LedgerRequest ledgerRequest);

    WithdrawResponse withdraw(LedgerRequest ledgerRequest);

    BalanceResponse checkBalance(String accountNo);

    List<TransactionHistoryResponse> viewHistory(String accountNo, String fromDate, String toDate) throws ParseException;
}
