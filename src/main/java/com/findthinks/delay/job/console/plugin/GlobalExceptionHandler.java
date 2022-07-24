package com.findthinks.delay.job.console.plugin;

import com.findthinks.delay.job.share.enums.ExceptionEnum;
import com.findthinks.delay.job.share.exception.DelayJobException;
import com.findthinks.delay.job.share.result.FoxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author YuBo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle business exception and wrap response
     *
     * @param cause
     * @return
     */
    @ExceptionHandler(value = DelayJobException.class)
    public FoxResult error(DelayJobException cause) {
        LOG.error("Business exception, errorCode: {}, errorDesc: {}", cause.getExceptionEnum().getCode(), cause.getMessage());
        return FoxResult.fail(cause.getExceptionEnum());
    }

    /**
     * Handle violation exception
     *
     * @param cve
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public FoxResult error(ConstraintViolationException cve) {
        LOG.error("Params violation excetion", cve);
        Set<ConstraintViolation<?>> constraintViolations = cve.getConstraintViolations();
        List<String> errorMsg = new LinkedList<>();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            for (ConstraintViolation<?> violation : constraintViolations) {
                errorMsg.add(violation.getMessage());
            }
        }
        return FoxResult.fail(ExceptionEnum.INVALID_PARAMS, errorMsg.toString());
    }

    @ExceptionHandler(value = BindException.class)
    public FoxResult error(BindException bindException) {
        LOG.error("Params bind exception", bindException);
        List<String> errorMsg = new LinkedList<>();
        for (ObjectError objectError : bindException.getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            errorMsg.add(fieldError.getField() + fieldError.getDefaultMessage());
        }
        return FoxResult.fail(ExceptionEnum.INVALID_PARAMS, errorMsg.toString());
    }

    /**
     * Handle uncatched exception, wrap a default response
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public FoxResult error(Exception ex) {
        LOG.error("Unkown exception", ex);
        return FoxResult.fail();
    }
}
