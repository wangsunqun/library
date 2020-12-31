package com.wsq.util.common;

import org.junit.Test;

import java.util.Map;

class JsonTests {

    @Test
    public void testJackson() {
        JsonUtil.from("", Map.class);
    }
}