package com.wsq.network.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * http执行器
 *
 * @author wsq
 * 2020/12/31 17:02
 */
@Data
@AllArgsConstructor
public class Executor {
    private int timeout;
    private boolean log;
    private int retries;


}
