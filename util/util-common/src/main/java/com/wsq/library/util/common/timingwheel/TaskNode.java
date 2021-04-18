package com.wsq.library.util.common.timingwheel;

import lombok.Data;


@Data
public class TaskNode {
    public TaskNode pre;
    public TaskNode next;

    private Runnable task;

    private long expire;
    private Bucket bucket;

    public TaskNode(Runnable task, long expire) {
        this.task = task;
        this.expire = System.currentTimeMillis() + expire;
    }
}
