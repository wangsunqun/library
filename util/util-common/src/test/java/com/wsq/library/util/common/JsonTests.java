package com.wsq.library.util.common;

import com.wsq.library.util.common.json.JsonUtils;
import org.junit.Test;

import java.util.Map;

class JsonTests {

    @Test
    public void testJackson() {
        JsonUtils.from("", Map.class);
    }
}