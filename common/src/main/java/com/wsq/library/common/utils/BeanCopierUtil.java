package com.wsq.library.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * cglib高性能拷贝类属性工具封装类
 * 注意：字段没有泛型，那么要类型和名字一样才能拷贝，如果有泛型，那么会泛型擦除要注意！！！
 *
 * @author wsq
 * 2021/2/7 13:44
 */
public class BeanCopierUtil {
    /**
     * 创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能
     */
    private static final Map<CopierIdentity, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    /**
     * 该方法没有自定义Converter,简单进行常规属性拷贝
     *
     * @param srcObj  源对象
     * @param destObj 目标对象
     */
    public static void copy(final Object srcObj, final Object destObj) {
        if (Objects.isNull(srcObj) || Objects.isNull(destObj)) {
            throw new RuntimeException("参数为空");
        }

        Class<?> sourceClass = srcObj.getClass();
        Class<?> targetClass = destObj.getClass();

        CopierIdentity key = new CopierIdentity(sourceClass, targetClass);
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);
    }

    @Data
    @AllArgsConstructor
    private static class CopierIdentity {
        private Class<?> source;
        private Class<?> target;
    }
}
