package com.wsq.library.timingwheel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
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
