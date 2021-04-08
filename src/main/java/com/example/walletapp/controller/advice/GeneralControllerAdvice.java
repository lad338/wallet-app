package com.example.walletapp.controller.advice;

import com.example.walletapp.exception.*;
import com.example.walletapp.model.api.response.GeneralErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GeneralControllerAdvice {

  @ExceptionHandler({ UserNotFoundException.class })
  @ResponseBody
  public ResponseEntity<GeneralErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
    final GeneralErrorResponse response = GeneralErrorResponse
      .builder()
      .message("User Not Found")
      .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler({ InvalidLoginException.class })
  @ResponseBody
  public ResponseEntity<GeneralErrorResponse> handleInvalidLoginException(InvalidLoginException e) {
    final GeneralErrorResponse response = GeneralErrorResponse
      .builder()
      .message("Invalid Login Credentials")
      .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({ InvalidUserTokenException.class })
  @ResponseBody
  public ResponseEntity<GeneralErrorResponse> handleInvalidUserTokenException(
    InvalidUserTokenException e
  ) {
    final GeneralErrorResponse response = GeneralErrorResponse
      .builder()
      .message("Unauthorized")
      .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler({ InsufficientAmountException.class })
  @ResponseBody
  public ResponseEntity<GeneralErrorResponse> handleInsufficientAmountException(
    InsufficientAmountException e
  ) {
    final GeneralErrorResponse response = GeneralErrorResponse
      .builder()
      .message("InsufficientAmount")
      .build();
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler({ UnexpectedException.class })
  @ResponseBody
  public ResponseEntity<GeneralErrorResponse> handleUnexpectedException(UnexpectedException e) {
    final GeneralErrorResponse response = GeneralErrorResponse
      .builder()
      .message(e.getMessage())
      .build();
    return ResponseEntity.badRequest().body(response);
  }
}
