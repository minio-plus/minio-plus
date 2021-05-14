package org.quantum.minio.plus.web.controller.advice;

import org.apache.commons.lang3.StringUtils;
import org.quantum.minio.plus.Response;
import org.quantum.minio.plus.ServiceException;
import org.quantum.minio.plus.ServiceExceptionState;
import org.quantum.minio.plus.ValueResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ike
 * @date 2021 年 05 月 11 日 13:59
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAdvice {

    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    public Response handlerException(ServiceException e) {
        Response r = new Response();
        r.setCode(e.getCode());
        return r;
    }

    /**
     * 绑定异常(实体对象传参)
     *
     * @param e BindException
     * @return R
     */
    @ExceptionHandler(BindException.class)
    public ValueResponse<Map<String, String>> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return ValueResponse.of(ServiceExceptionState.BAD_PARAMETER, this.getFieldErrorMap(fieldErrors));
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return R
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ValueResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return ValueResponse.of(ServiceExceptionState.BAD_PARAMETER, message);
    }

    /**
     * 入参校验@Valid异常处理,添加此处理器后,在方法入参出就不需要写BindResult errors了,会自动处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValueResponse<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return ValueResponse.of(ServiceExceptionState.BAD_PARAMETER, this.getFieldErrorMap(fieldErrors));
    }

    /**
     * 转map
     * @param fieldErrors
     * @return
     */
    private Map<String, String> getFieldErrorMap(List<FieldError> fieldErrors) {
        Map<String, String> map = new HashMap<>();
        for (FieldError error : fieldErrors) {
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }
}
