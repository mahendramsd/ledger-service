package com.msd.ledgerservice.exception;


import com.msd.ledgerservice.dto.AuthErrorDto;
import com.msd.ledgerservice.util.CustomErrorCodes;
import com.msd.ledgerservice.util.DynamicErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> resourceNotFoundException(Exception ex, WebRequest request) {
    // ErrorDetails errorDetails =
    // new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
    // ErrorDetails errorDetails =
    // new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> customExceptionHandler(CustomException customException) {
    CustomErrorCodes customErrorCodes = customException.getCustomErrorCodes();
    AuthErrorDto authErrorDto =
            new AuthErrorDto(customErrorCodes.getId(), customErrorCodes.getMsg());
    return new ResponseEntity<>(authErrorDto, customErrorCodes.getHttpCode());
  }

  @ExceptionHandler(DynamicException.class)
  public ResponseEntity<?> dynamicExceptionHandler(DynamicException dynamicException) {
    DynamicErrorCodes dynamicErrorCodes = dynamicException.getDynamicErrorCodes();
    AuthErrorDto authErrorDto =
            new AuthErrorDto(dynamicErrorCodes.getId(), dynamicErrorCodes.getMsg());
    return new ResponseEntity<>(authErrorDto, dynamicErrorCodes.getHttpCode());
  }

}

