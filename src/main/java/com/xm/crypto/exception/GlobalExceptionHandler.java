package com.xm.crypto.exception;

import com.xm.crypto.model.dto.ErrorCode;
import com.xm.crypto.model.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.xm.crypto.model.dto.ErrorCode.INTERNAL_ERROR;
import static com.xm.crypto.model.dto.ErrorCode.INVALID_PARAMETER;
import static com.xm.crypto.model.dto.ErrorCode.MISSING_PARAMETER;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        logger.debug("Requested resource is not found.", exception);
        ErrorResponse error = new ErrorResponse(
                ErrorCode.RESOURCE_NOT_FOUND,
                "The requested resource is not found."
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException exception) {
        String parameterName = exception.getParameterName();
        logger.debug("Request parameter '{}' is missing.", parameterName, exception);

        ErrorResponse error = new ErrorResponse(
                MISSING_PARAMETER,
                "Required parameter '" + parameterName + "' is missing."
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String parameterName = exception.getParameter().getParameterName();
        logger.debug("Method argument type mismatch for parameter '{}'.", parameterName, exception);

        ErrorResponse error = new ErrorResponse(
                INVALID_PARAMETER,
                "An invalid value was provided for '" + parameterName + "'parameter."
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        logger.error("Unexpected error occurred.", exception);
        ErrorResponse error = new ErrorResponse(
                INTERNAL_ERROR,
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
