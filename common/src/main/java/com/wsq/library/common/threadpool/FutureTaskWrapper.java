package com.wsq.library.common.threadpool;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class FutureTaskWrapper<V> extends FutureTask<V> {
    private Runnable doneHandle;
    private Consumer<Throwable> exceptionHandle;

    public FutureTaskWrapper(Callable<V> callable) {
        this(callable, null, null);
    }

    public FutureTaskWrapper(Runnable runnable) {
        this(runnable, null, null);
    }

    public FutureTaskWrapper(Callable<V> callable, Runnable doneHandle, Consumer<Throwable> exceptionHandle) {
        super(callable);
        this.doneHandle = doneHandle;
        this.exceptionHandle = Objects.nonNull(exceptionHandle) ? exceptionHandle : throwable -> {
            throw new RuntimeException(throwable);
        };
    }

    public FutureTaskWrapper(Runnable runnable, Runnable doneHandle, Consumer<Throwable> exceptionHandle) {
        super(runnable, null);
        this.doneHandle = doneHandle;
        this.exceptionHandle = Objects.nonNull(exceptionHandle) ? exceptionHandle : throwable -> {
            throw new RuntimeException(throwable);
        };
    }

    @Override
    protected void done() {
        if (Objects.isNull(doneHandle)) return;
        doneHandle.run();
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);

        if (Objects.isNull(exceptionHandle)) return;
        exceptionHandle.accept(t);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            return super.get();
        } catch (ExecutionException ignored) {

        }

        return null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return super.get(timeout, unit);
        } catch (ExecutionException ignored) {

        }

        return null;
    }

    public Runnable getDoneHandle() {
        return doneHandle;
    }

    public void setDoneHandle(Runnable doneHandle) {
        this.doneHandle = doneHandle;
    }

    public Consumer<Throwable> getExceptionHandle() {
        return exceptionHandle;
    }

    public void setExceptionHandle(Consumer<Throwable> exceptionHandle) {
        this.exceptionHandle = exceptionHandle;
    }
}
