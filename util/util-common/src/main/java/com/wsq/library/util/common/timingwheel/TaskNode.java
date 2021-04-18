package com.wsq.library.util.common.timingwheel;

import lombok.Data;


@Data
public class TaskNode {
    public TaskNode pre;
    public TaskNode next;

    private Runnable task;

    private long expireAt;
    private Bucket bucket;

    public TaskNode(Runnable task, long expire) {
        this.task = task;
        this.expireAt = System.currentTimeMillis() + expire;
    }
}
