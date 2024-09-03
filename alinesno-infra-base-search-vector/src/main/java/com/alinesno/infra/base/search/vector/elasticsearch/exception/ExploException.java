package com.alinesno.infra.base.search.vector.elasticsearch.exception;

public class ExploException extends RuntimeException {

    public ExploException(String message) {
        super(message);
    }

    public ExploException(String message, String errorMsg) {
        super(message, new Throwable(errorMsg));
    }

    public ExploException(String message, Throwable cause) {
        super(message, cause);
    }
}
