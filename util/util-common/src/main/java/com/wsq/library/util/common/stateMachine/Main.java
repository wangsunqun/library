package com.wsq.library.util.common.stateMachine;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wsq
 * 2021/4/22 13:49
 */
public class Main {
    public static void main(String[] args) {

    }

    @AllArgsConstructor
    public enum EventEnum {
        PASS(1),
        UN_PASS(2);

        public int id;
    }

    @AllArgsConstructor
    public enum StatusEnum {
        INIT(1),
        SUCCESS(2),
        FAIL(3);

        public int id;
    }
}
