package com.wsq.network.http;

public class Http {
    private static final int DEFAULT_TIMEOUT = 60 * 1000;
    private static final boolean DEFAULT_LOG = true;
    private static final int DEFAULT_RETRIES = 0;
    private static Executor DEFAULT_EXECUTOR = new Executor(DEFAULT_TIMEOUT, DEFAULT_LOG, DEFAULT_RETRIES);


}