package com.wsq.library.cache;

import com.github.benmanes.caffeine.cache.Cache;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 主流的几个cache都加了final，无法继承，只能写一个装饰者了
 */
public final class CacheWrapper<V> {
    private int maxFreeMemory = 256 * 1024 * 1024;

    private final Cache<String, V> cache;

    public CacheWrapper(Cache<String, V> cache) {
        this.cache = cache;
    }

    public CacheWrapper<V> cacheLimit(int maxFreeMemory) {
        this.maxFreeMemory = maxFreeMemory;
        return this;
    }

    public boolean hasRemainedMemory() {
        return MemoryUtil.maxAvailable > maxFreeMemory;
    }

    public void put(String key, V value) {
        if (hasRemainedMemory()) cache.put(key, value);
    }

    public void putAll(Map<String, V> map) {
        if (hasRemainedMemory()) cache.putAll(map);
    }

    private V get(String key, Function<String, V> function) {
        return cache.get(key, function);
    }

    private V getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    private Map<String, V> getAll(Collection<String> keyList, Function<Iterable<? extends String>, Map<String, V>> function) {
        return cache.getAll(keyList, function);
    }

    private Map<String, V> getAllPresent(Collection<String> keyList) {
        return cache.getAllPresent(keyList);
    }

    public static class MemoryUtil {
        private static final MemoryMXBean MX_BEAN = ManagementFactory.getMemoryMXBean();

        private static volatile long maxAvailable;

        private static final ScheduledExecutorService SCHEDULED = Executors.newSingleThreadScheduledExecutor();

        static {
            SCHEDULED.scheduleWithFixedDelay(MemoryUtil::refresh, 0, 50, TimeUnit.MILLISECONDS);
        }

        private static void refresh() {
            final MemoryUsage memoryUsage = MX_BEAN.getHeapMemoryUsage();
            maxAvailable = memoryUsage.getCommitted();
        }
    }
}
