package com.example.security.config;

import java.sql.SQLException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.webjars.NotFoundException;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.common.dto.response.ErrorResponse;
import com.example.common.dto.response.ValidationErrorResponse;

@ControllerAdvice
public class ExceptionConfig {
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    setErrorInfo(error, "API-001", e.getMessage());
    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
      error.getValidateDetails().put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return error;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    setErrorInfo(error, "API-002", e.getMessage());
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      error.getValidateDetails().put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return error;
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  ErrorResponse onNotFoundException(NotFoundException e) {
    ErrorResponse error = new ErrorResponse();
    setErrorInfo(error, "API-003", e.getMessage());
    return error;
  }
  
  @ExceptionHandler(DuplicateKeyException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  ErrorResponse onDuplicateKeyException(DuplicateKeyException e) {
    ErrorResponse error = new ErrorResponse();
    setErrorInfo(error, "API-004", e.getMessage());
    return error;
  }
  
  @ExceptionHandler(NotImplementedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  ErrorResponse onNotImplementException(NotImplementedException e) {
    ErrorResponse error = new ErrorResponse();
    setErrorInfo(error, "API-005", e.getMessage());
    return error;
  }
  
  @ExceptionHandler(SQLException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  ErrorResponse onSQLException(SQLException e) {
    ErrorResponse error = new ErrorResponse();
    setErrorInfo(error, "API-006", e.getMessage());
    return error;
  }
  
  private void setErrorInfo(ErrorResponse error, String errorCode, String defaultMessage) {
    error.setErrorCode(errorCode);
    String msg = Messages.getString("api.error." + errorCode, defaultMessage);
    error.setErrorMsg(msg);
  }
}
