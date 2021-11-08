package com.msd.ledgerservice.controller;

import com.msd.ledgerservice.dto.request.LedgerRequest;
import com.msd.ledgerservice.dto.response.BalanceResponse;
import com.msd.ledgerservice.dto.response.DepositResponse;
import com.msd.ledgerservice.dto.response.TransactionHistoryResponse;
import com.msd.ledgerservice.dto.response.WithdrawResponse;
import com.msd.ledgerservice.service.LedgerService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/ledger")
@Api(value = "Ledger API End Point")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @ApiOperation(value = "Deposit for Bank Account | Credit Account - Customer Bank Account, Debit Account - Bank CashBook Account", response = DepositResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", value = "application/json", paramType = "header"),
            @ApiImplicitParam(name = "bearer-access-token", value = "Bearer Generated access token",
                    paramType = "header")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully Deposit to Bank Account"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403,
                    message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DepositResponse> deposit(@RequestBody LedgerRequest ledgerRequest) {
        return ResponseEntity.ok(ledgerService.deposit(ledgerRequest));
    }

    @ApiOperation(value = "withdrawal in Bank Account | Credit Account - Bank CashBook Account, Debit Account - Customer Bank Account", response = WithdrawResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", value = "application/json", paramType = "header"),
            @ApiImplicitParam(name = "bearer-access-token", value = "Bearer Generated access token",
                    paramType = "header")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully Withdraw in Bank Account"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403,
                    message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody LedgerRequest ledgerRequest) {
        return ResponseEntity.ok(ledgerService.withdraw(ledgerRequest));
    }

    @ApiOperation(value = "Balance in Customer Bank Account", response = BalanceResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", value = "application/json", paramType = "header"),
            @ApiImplicitParam(name = "bearer-access-token", value = "Bearer Generated access token",
                    paramType = "header")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully Withdraw in Bank Account"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403,
                    message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BalanceResponse> withdraw(@RequestParam("accountNo") String accountNo) {
        return ResponseEntity.ok(ledgerService.checkBalance(accountNo));
    }

    @ApiOperation(value = "Transaction history in Bank Account", response = TransactionHistoryResponse.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Content-Type", value = "application/json", paramType = "header"),
            @ApiImplicitParam(name = "bearer-access-token", value = "Bearer Generated access token",
                    paramType = "header")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully Withdraw in Bank Account"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403,
                    message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionHistoryResponse>> transactionHistory(
            @RequestParam("accountNo") String accountNo,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate) throws ParseException {
        return ResponseEntity.ok(ledgerService.viewHistory(accountNo,fromDate,toDate));
    }


}
