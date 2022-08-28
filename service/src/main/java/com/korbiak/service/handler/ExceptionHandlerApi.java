package com.korbiak.service.handler;

import com.korbiak.service.cache.CacheException;
import com.korbiak.service.handler.error.ApiError;
import com.korbiak.service.handler.error.ApiSubError;
import com.korbiak.service.handler.error.ApiValidationError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common error handling
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionHandlerApi extends ResponseEntityExceptionHandler {

    /**
     * Error handling for all IllegalArgumentExceptions
     * @param exception IllegalArgumentException
     * @return ApiError
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleIllegalArgumentExceptions(IllegalArgumentException exception) {
        return getApiError(exception, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error handling for all RestClientException
     * @param exception RestClientException
     * @return ApiError
     */
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiError handleRestClientException(RestClientException exception) {
        return getApiError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Error handling for all CacheException
     * @param exception CacheException
     * @return ApiError
     */
    @ExceptionHandler(CacheException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ApiError handleCacheException(CacheException exception) {
        return getApiError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    protected ApiError handleEntityNotFound(EntityNotFoundException exception) {
//        return getApiError(exception, HttpStatus.NOT_FOUND);
//    }

    /**
     * Error handling for all MethodArgumentNotValidException
     * @param exception MethodArgumentNotValidException
     * @return ResponseEntity<Object>
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message("Validation error!")
                .subErrors(getMANVESubErrors(exception))
                .build();
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private List<ApiSubError> getMANVESubErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::convertToValidationError)
                .collect(Collectors.toList());
    }

    private ApiValidationError convertToValidationError(FieldError fieldError) {
        return ApiValidationError
                .builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build();
    }

    private ApiError getApiError(Exception e, HttpStatus httpStatus) {
        return ApiError.builder()
                .status(httpStatus)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }
}
