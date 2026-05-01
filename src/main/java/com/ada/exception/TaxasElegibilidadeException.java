package com.ada.exception;

public class TaxasElegibilidadeException extends RuntimeException {
    public TaxasElegibilidadeException(String message) {
        super(message);
    }

    public TaxasElegibilidadeException(String message, Throwable cause) {
        super(message, cause);
    }
}
