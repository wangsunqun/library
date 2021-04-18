package com.wsq.library.util.common.timingwheel;

import java.util.concurrent.DelayQueue;
import java.util.stream.IntStream;

public class TimingWheel {
    /**
     * 一个时间槽的范围
     */
    private long tickMis;

    /**
     * 时间轮大小
     */
    private int wheelSize;

    /**
     * 时间跨度
     */
    private long interval;

    /**
     * 时间槽
     */
    private Bucket[] buckets;

    /**
     * 游标,当前时间
     */
    private long currentTime;

    /**
     * 上层时间轮
     */
    private volatile TimingWheel overflowWheel;

    private DelayQueue<Bucket> delayQueue;

    public TimingWheel(long tickMis, int wheelSize, long currentTime, TimingWheel overflowWheel, DelayQueue<Bucket> delayQueue) {
        this.tickMis = tickMis;
        this.wheelSize = wheelSize;
        this.interval = tickMis * wheelSize;
        this.buckets = new Bucket[wheelSize];
        this.currentTime = currentTime - (currentTime % tickMis);
        this.overflowWheel = overflowWheel;
        this.delayQueue = delayQueue;

        IntStream.rangeClosed(0, wheelSize - 1).forEach(i -> buckets[i] = new Bucket());
    }

    public boolean add(TaskNode taskNode) {
        long expire = taskNode.getExpire();

        // 向上取整，解释一下为什么要向上
        // 比如我们时间间隔是10s，currentTime是100s， 当前是109s，如果不是取110s那么新增101s的任务就不会执行
        if (expire < currentTime + tickMis) {
            //过期任务直接执行
            return false;
        }

        if (expire < currentTime + interval) {
            long virtualId = expire / tickMis;
            int index = (int) (virtualId % wheelSize);

            Bucket bucket = buckets[index];
            bucket.add(taskNode);

            if (bucket.setExpire(virtualId * tickMis)) {
                delayQueue.offer(bucket);
            }
        } else {
            TimingWheel overflowWheel = getOverflowWheel();
            overflowWheel.add(taskNode);
        }

        return true;
    }

    /**
     * 创建或者获取上层时间轮
     */
    private TimingWheel getOverflowWheel() {
        if (null == overflowWheel) {
            synchronized (this) {
                overflowWheel = new TimingWheel(interval, wheelSize, currentTime, overflowWheel, delayQueue);
            }
        }

        return overflowWheel;
    }

    /**
     * 推进时间
     */
    public void advanceClock(long timestamp) {
        if (timestamp >= currentTime + tickMis) {
            currentTime = timestamp - (timestamp % tickMis);
            if (overflowWheel != null) {
                getOverflowWheel().advanceClock(timestamp);
            }
        }
    }
}
