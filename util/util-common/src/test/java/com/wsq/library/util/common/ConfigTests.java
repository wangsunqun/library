package com.wsq.library.util.common;

import com.wsq.library.util.common.config.ConfigUtils;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.core.env.Profiles.of;

public class ConfigTests {
    @Test
    public void getConfigsByKeyPre1() {
        Map<String, Object> json = ConfigUtils.getConfigsByKeyPre("json");
        System.out.println(json);
    }

    @Test
    public void getConfigsByKeyPre2() {
        TestObject json = ConfigUtils.getConfigsByKeyPre("json", TestObject.class);
        System.out.println(json);
    }

    @Data
    public static class TestObject {
        private String key;
        private String value;


    }
}
