package com.wsq.library.network.http;

import com.wsq.library.network.http.executor.Executor;
import org.junit.Test;

public class HttpTest {

    @Test
    public void httpTest() {
        Executor executor = Http.disableLog().timeout(123);
        Executor executor1 = Http.timeout(123).disableLog();
        Executor executor2 = Http.timeout(456).retryDefault(3,5);
        System.out.println();
    }
}