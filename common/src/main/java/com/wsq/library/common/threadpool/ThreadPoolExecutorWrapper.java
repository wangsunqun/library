package com.wsq.library.common.threadpool;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * futuretask版本
 */
public class ThreadPoolExecutorWrapper {
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolExecutorWrapper buildBlockTPWithTimeout(int corePoolSize,
                                                                    int maximumPoolSize, long timeout, TimeUnit unit) {
        return build(corePoolSize, maximumPoolSize, 100, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), Executors.defaultThreadFactory(), (r, executor) -> {
            try {
                executor.getQueue().offer(r, timeout, unit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static ThreadPoolExecutorWrapper buildBlockTP(int corePoolSize,
                                                         int maximumPoolSize) {
        return build(corePoolSize, maximumPoolSize, 100, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), Executors.defaultThreadFactory(), (r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static ThreadPoolExecutorWrapper build(int corePoolSize,
                                                  int maximumPoolSize,
                                                  long keepAliveTime,
                                                  TimeUnit unit,
                                                  BlockingQueue<Runnable> workQueue,
                                                  ThreadFactory threadFactory,
                                                  RejectedExecutionHandler handler) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = new ThreadPoolExecutorWrapper();
        threadPoolExecutorWrapper.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        return threadPoolExecutorWrapper;
    }

    public void execute(Runnable command) {
        execute(command, null, null);
    }

    public void execute(Runnable command, Runnable doneHandle) {
        execute(command, doneHandle, null);
    }

    public void execute(Runnable command, Consumer<Throwable> exceptionHandle) {
        execute(command, null, exceptionHandle);
    }

    public void execute(Runnable command, Runnable doneHandle, Consumer<Throwable> exceptionHandle) {
        FutureTaskWrapper<?> ft = new FutureTaskWrapper<>(command, doneHandle, exceptionHandle);
        threadPoolExecutor.execute(ft);
    }

    public <V> FutureTaskWrapper<V> submit(Callable<V> callable) {
        return submit(callable, null, null);
    }

    public <V> FutureTaskWrapper<V> submit(Callable<V> callable, Runnable doneHandle) {
        return submit(callable, doneHandle, null);
    }

    public <V> FutureTaskWrapper<V> submit(Callable<V> callable, Consumer<Throwable> exceptionHandle) {
        return submit(callable, null, exceptionHandle);
    }

    public <V> FutureTaskWrapper<V> submit(Callable<V> callable, Runnable doneHandle, Consumer<Throwable> exceptionHandle) {
        FutureTaskWrapper<V> ft = new FutureTaskWrapper<>(callable, doneHandle, exceptionHandle);
        threadPoolExecutor.submit(ft);
        return ft;
    }
}
