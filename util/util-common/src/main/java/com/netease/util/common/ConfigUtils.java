package com.netease.util.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    private static Properties root = new Properties();
    private static Properties rootProp = new Properties();
    private static Properties rootYml = new Properties();

    static {
        // properties
        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream("application.properties");
        if (ObjectUtils.isNotEmpty(inputStream)) {
            try {
                rootProp.load(inputStream);
            } catch (Exception e) {
                throw new RuntimeException("加载application.properties出错!");
            }
        }

        // yml
        Resource resource = new ClassPathResource("application.yml");
        if (resource.exists()) {
            try {
                YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
                yamlFactory.setResources(resource);
                rootYml = yamlFactory.getObject();
            } catch (Exception e) {
                throw new RuntimeException("加载application.yml出错!");
            }
        }

        root.putAll(rootYml);
        root.putAll(rootProp);
    }

    public static <T> T getObject(String key, Class<T> clazz) {
        String value = getString(key);
        return StringUtils.isNotBlank(value) ? JsonUtil.from(value, clazz) : null;
    }

    public static String getString(String key) {
        return root.getProperty(key);
    }

    public static String getStringDefault(String key, String defaultValue) {
        String value = getString(key);
        return StringUtils.isNotBlank(value) ? value : defaultValue;
    }

    public static Integer getInt(String key) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Integer.parseInt(value) : null;
    }

    public static Integer getIntDefault(String key, Integer defaultValue) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Integer.parseInt(value) : defaultValue;
    }

    public static Long getLong(String key) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Long.parseLong(value) : null;
    }

    public static Long getLongDefault(String key, Long defaultValue) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Long.parseLong(value) : defaultValue;
    }

    public static Float getFloat(String key) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Float.parseFloat(value) : null;
    }

    public static Float getFloatDefault(String key, Float defaultValue) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Float.parseFloat(value) : defaultValue;
    }

    public static Double getDouble(String key) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Double.parseDouble(value) : null;
    }

    public static Double getDoubleDefault(String key, Double defaultValue) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Double.parseDouble(value) : defaultValue;
    }

    public static Boolean getBoolean(String key) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Boolean.parseBoolean(value) : null;
    }

    public static Boolean getBooleanDefault(String key, Boolean defaultValue) {
        String value = getString(key);
        return ObjectUtils.isNotEmpty(value) ? Boolean.parseBoolean(value) : defaultValue;
    }

//    public static Map<String, String> getConfigsByKeyPre(String key) {
//
//    }
}
