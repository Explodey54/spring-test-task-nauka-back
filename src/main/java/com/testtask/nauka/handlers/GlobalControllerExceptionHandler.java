package com.testtask.nauka.handlers;

import com.testtask.nauka.common.exceptions.InvalidFieldsException;
import com.testtask.nauka.common.response.ErrorResponse;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFieldsException.class)
    @ResponseBody
    public ErrorResponse handleException(InvalidFieldsException e) {
        return new ErrorResponse(e.getMessage(), e.getFields());
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(ResponseStatusException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getReason()), HttpStatus.valueOf(e.getStatus().value())
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(NoHandlerFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(MethodArgumentNotValidException e) {
        Map<String, List<String>> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(i -> {
            List<String> errorFieldList = errorMap.computeIfAbsent(i.getField(), k -> new ArrayList<>());
            errorFieldList.add(i.getDefaultMessage());
        });
        String reason = String.format("Validation failed for field(s): %s", String.join(", ", errorMap.keySet()));
        return new ErrorResponse(reason, errorMap);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleException(NestedRuntimeException e) {
        return new ErrorResponse(e.getMostSpecificCause().getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleException(Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
