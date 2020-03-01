package com.shaylee.core.exception;

import lombok.Data;

/**
 * Title: BaseException
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-02-26
 */
@Data
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -5984862783078783127L;

    protected String code;

    public BaseException() {
        super();
    }

    public BaseException(String code) {
        super();
        this.code = code;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }
}
