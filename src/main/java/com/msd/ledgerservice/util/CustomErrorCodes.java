package com.msd.ledgerservice.util;

import org.springframework.http.HttpStatus;

public enum CustomErrorCodes {
	USER_DISABLED(1000, "USER_DISABLED", HttpStatus.BAD_REQUEST),
	INVALID_CREDENTIALS(1001, "INVALID_CREDENTIALS", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND(1002, "INVALID_CREDENTIALS", HttpStatus.NOT_FOUND),
	INSUFFICIENT_BALANCE(1003, "Withdraw Can't Process, Balance Insufficient", HttpStatus.BAD_REQUEST),
	INSUFFICIENT_AMOUNT(1004, "Ledger Amount Insufficient", HttpStatus.CONFLICT),
	CREDIT_FAIL(1005, "Credit Entry Failed", HttpStatus.INTERNAL_SERVER_ERROR),
	DEBIT_FAIL(1005, "Debit Entry Failed", HttpStatus.INTERNAL_SERVER_ERROR);

	private final int id;
	private final String msg;
	private final HttpStatus httpCode;

	CustomErrorCodes(int id, String msg, HttpStatus httpCode) {
		this.id = id;
		this.msg = msg;
		this.httpCode = httpCode;
	}

	public int getId() {
		return this.id;
	}

	public String getMsg() {
		return this.msg;
	}

	public HttpStatus getHttpCode() {
		return this.httpCode;
	}

}
