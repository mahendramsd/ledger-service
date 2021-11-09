package com.msd.ledgerservice.service.impl;

import com.msd.ledgerservice.domain.Ledger;
import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.BalanceResponse;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.dto.response.TransactionHistoryResponse;
import com.msd.ledgerservice.dto.response.WithdrawResponse;
import com.msd.ledgerservice.exception.CustomException;
import com.msd.ledgerservice.repositories.LedgerRepository;
import com.msd.ledgerservice.service.LedgerService;
import com.msd.ledgerservice.util.Constants;
import com.msd.ledgerservice.util.CreditDebitStatus;
import com.msd.ledgerservice.util.CustomErrorCodes;
import com.msd.ledgerservice.util.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LedgerServiceImpl implements LedgerService {

    private static final Logger logger = LoggerFactory.getLogger(LedgerServiceImpl.class);

    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    private final LedgerRepository ledgerRepository;

    public LedgerServiceImpl(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public DepositResponse deposit(LedgerRequest ledgerRequest) {

        logger.debug("Try To Deposit  {}", ledgerRequest.getAmount());
        if (ledgerRequest.getAmount().compareTo(BigDecimal.ZERO) == 1) {
            // Create credit entry | Customer Account Credit
                BigDecimal balance = getBalance(ledgerRequest.getCustomerAccount()).add(ledgerRequest.getAmount());
            Ledger ledgerC = generateLedger(ledgerRequest, ledgerRequest.getCustomerAccount(), balance, CreditDebitStatus.CREDIT);
            logger.debug("Add credit entry {}", ledgerRequest.getCustomerAccount());
            if (ledgerC.getId() > 0L) {
                // Create Debit entry | Bank caseBook Debit
                BigDecimal cashBookBalance = getBalance(ledgerRequest.getCashBookAccount()).subtract(ledgerRequest.getAmount());
                Ledger ledgerD = generateLedger(ledgerRequest, ledgerRequest.getCashBookAccount(),cashBookBalance, CreditDebitStatus.DEBIT);
                logger.debug("Add Debit entry {}", ledgerRequest.getCashBookAccount());
                if (ledgerD.getId() > 0L) {
                    DepositResponse depositResponse = new DepositResponse();
                    depositResponse.setAmount(ledgerRequest.getAmount());
                    depositResponse.setMessage(Constants.DEPOSIT_MESSAGE);
                    depositResponse.setCustomerAccount(ledgerRequest.getCustomerAccount());
                    depositResponse.setCashBookAccount(ledgerRequest.getCashBookAccount());
                    depositResponse.setTransactionNo(ledgerRequest.getTransactionNo());
                    depositResponse.setDate(ledgerRequest.getLedgerDate());
                    depositResponse.setCreatedDate(ledgerD.getCreatedDate());
                    depositResponse.setBalance(ledgerC.getBalance());
                    logger.debug("Deposit Successfully {}");
                    return depositResponse;
                } else {
                    throw new CustomException(CustomErrorCodes.DEBIT_FAIL);
                }
            } else {
                throw new CustomException(CustomErrorCodes.CREDIT_FAIL);
            }
        } else {
            throw new CustomException(CustomErrorCodes.INSUFFICIENT_AMOUNT);
        }
    }

    @Override
    public WithdrawResponse withdraw(LedgerRequest ledgerRequest) {
        logger.debug("Try To Withdraw  {}", ledgerRequest.getAmount());
        if (getBalance(ledgerRequest.getCustomerAccount()).compareTo(ledgerRequest.getAmount()) == 1) {
            BigDecimal balance = getBalance(ledgerRequest.getCashBookAccount()).add(ledgerRequest.getAmount());
            // Create Credit entry | Bank caseBook Credit
            Ledger ledgerC = generateLedger(ledgerRequest, ledgerRequest.getCashBookAccount(),balance, CreditDebitStatus.CREDIT);
            logger.debug("Add credit entry {}", ledgerRequest.getCashBookAccount());
            if (ledgerC.getId() > 0L) {
                BigDecimal cashBookBalance = getBalance(ledgerRequest.getCustomerAccount()).subtract(ledgerRequest.getAmount());
                // Create Debit entry | Customer Account Debit
                Ledger ledgerD = generateLedger(ledgerRequest, ledgerRequest.getCustomerAccount(),cashBookBalance, CreditDebitStatus.DEBIT);
                logger.debug("Add Debit entry {}", ledgerRequest.getCustomerAccount());
                if (ledgerD.getId() > 0L) {
                    WithdrawResponse withdrawResponse = new WithdrawResponse();
                    withdrawResponse.setAmount(ledgerRequest.getAmount());
                    withdrawResponse.setMessage(Constants.WITHDRAW_MESSAGE);
                    withdrawResponse.setCustomerAccount(ledgerRequest.getCustomerAccount());
                    withdrawResponse.setCashBookAccount(ledgerRequest.getCashBookAccount());
                    withdrawResponse.setTransactionNo(ledgerRequest.getTransactionNo());
                    withdrawResponse.setDate(ledgerRequest.getLedgerDate());
                    withdrawResponse.setCreatedDate(ledgerD.getCreatedDate());
                    withdrawResponse.setBalance(ledgerD.getBalance());
                    logger.debug("Withdraw Successfully {}");
                    return withdrawResponse;
                } else {
                    throw new CustomException(CustomErrorCodes.DEBIT_FAIL);
                }
            } else {
                throw new CustomException(CustomErrorCodes.CREDIT_FAIL);
            }
        } else {
            throw new CustomException(CustomErrorCodes.INSUFFICIENT_BALANCE);
        }
    }

    @Override
    public BalanceResponse checkBalance(String accountNo) {
       BigDecimal balance = getBalance(accountNo);
       return new BalanceResponse(accountNo,balance);
    }

    /**
     * Get Balance in Customer
     * @param accountNo
     * @return
     */
    public BigDecimal getBalance(String accountNo) {
        BigDecimal creditSum = BigDecimal.ZERO;
        BigDecimal debitSum = BigDecimal.ZERO;
         creditSum = ledgerRepository.findLedgerSum(accountNo,CreditDebitStatus.CREDIT,Status.ACTIVE);
         debitSum = ledgerRepository.findLedgerSum(accountNo,CreditDebitStatus.DEBIT,Status.ACTIVE);
        if (creditSum == null) {
             creditSum = BigDecimal.ZERO;
        }
        if (debitSum == null) {
            debitSum = BigDecimal.ZERO;
        }
        return creditSum.subtract(debitSum);
    }

    @Override
    public List<TransactionHistoryResponse> viewHistory(String accountNo, String fromDate, String toDate) throws ParseException {
        Date from = new Date();
        Date to = null;
        if(StringUtils.isNotEmpty(fromDate)) {
            from = simpleDateFormat.parse(fromDate);
        }
        if(StringUtils.isNotEmpty(toDate)) {
            to = simpleDateFormat.parse(toDate);
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(from);
            calendar.add(Calendar.MONTH,1);
            to = calendar.getTime();
        }
        List<Ledger> ledgers = ledgerRepository.findByAccountNoAndLedgerDateBetween(accountNo, from, to);
        return ledgers.stream().map(TransactionHistoryResponse:: new).collect(Collectors.toList());
    }

    /**
     * @param ledgerRequest
     * @param status
     * @param accountNo
     * @return
     */
    public Ledger generateLedger(LedgerRequest ledgerRequest, String accountNo, BigDecimal balance, CreditDebitStatus status) {
        Ledger ledger = new Ledger();
        ledger.setAmount(ledgerRequest.getAmount());
        ledger.setDescription(ledgerRequest.getDescription());
        ledger.setReferenceNo(ledgerRequest.getTransactionNo());
        ledger.setJournalNo(ledgerRequest.getJournalNo());
        ledger.setCurrencyCode(ledgerRequest.getCurrencyCode());
        ledger.setStatus(Status.ACTIVE);
        ledger.setLedgerDate(ledgerRequest.getLedgerDate());
        ledger.setBranchId(ledgerRequest.getBranchId());
        ledger.setAccountNo(accountNo);
        ledger.setCreditDebitStatus(status);
        ledger.setBalance(balance);
        return ledgerRepository.save(ledger);
    }
}
