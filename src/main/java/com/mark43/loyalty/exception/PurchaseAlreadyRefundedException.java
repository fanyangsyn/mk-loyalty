package com.mark43.loyalty.exception;

public class PurchaseAlreadyRefundedException extends RuntimeException {
    public PurchaseAlreadyRefundedException(String message) {
        super(message);
    }
}
