package com.msd.ledgerservice.exception;


import com.msd.ledgerservice.util.DynamicErrorCodes;

public class DynamicException extends RuntimeException {

  private DynamicErrorCodes dynamicErrorCodes;

  public DynamicException(DynamicErrorCodes dynamicErrorCodes) {
    this.dynamicErrorCodes = dynamicErrorCodes;
  }

  public DynamicErrorCodes getDynamicErrorCodes() {
    return dynamicErrorCodes;
  }
}
