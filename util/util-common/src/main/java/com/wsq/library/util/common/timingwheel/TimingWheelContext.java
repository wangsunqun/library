package com.wsq.library.util.common.timingwheel;

import java.util.concurrent.*;

public class TimingWheelContext {
    private int threadCore;
    private int threadMax;
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            threadCore,
            threadMax,
            1000,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(), (r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    });

    private final DelayQueue<Bucket> delayQueue = new DelayQueue<>();
    private final TimingWheel timingWheel;

    public TimingWheelContext(int threadCore, int threadMax) {
        this.threadCore = threadCore;
        this.threadMax = threadMax;
        timingWheel = new TimingWheel(100, 100, System.currentTimeMillis(), null, delayQueue);

        new Thread(() -> {
            while (true) {
                try {
                    // 100ms间隔获取任务，直到有任务了才会去更新时间轮的指针，类似惰性修改的味道
                    Bucket bucket = delayQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (bucket != null) {
                        timingWheel.advanceClock(bucket.getExpireAt().get());
                        bucket.handleTask(this::add);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void add(TaskNode task) {
        // 降级操作
        if (!timingWheel.add(task)) {
            threadPool.submit(task.getTask());
        }
    }

    public static void main(String[] args) {
        TimingWheelContext context = new TimingWheelContext(5, 10);

        TaskNode taskNode = new TaskNode(() -> System.out.println("执行指定任务完成!"), 5000);
        context.add(taskNode);
    }
}
