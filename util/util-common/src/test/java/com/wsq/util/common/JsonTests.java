package com.wsq.util.common;

import com.wsq.util.common.json.JsonUtils;
import org.junit.Test;

import java.util.Map;

class JsonTests {

    @Test
    public void testJackson() {
        JsonUtils.from("", Map.class);
    }
}