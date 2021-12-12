package com.wsq.library.gateway.starter.common.exception;

public class GatewayException extends RuntimeException {
    private int status;

    public GatewayException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
