package com.bob.utils;

/**
 * Created by wangxiang on 17/6/29.
 */
public class ReflectException extends RuntimeException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6213149635297151442L;

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}