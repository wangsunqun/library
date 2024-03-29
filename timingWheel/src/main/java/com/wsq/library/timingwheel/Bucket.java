package com.wsq.library.timingwheel;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Data
public class Bucket implements Delayed {
    // 过期时间
    private AtomicLong expireAt = new AtomicLong(-1);

    // 头结点
    private TaskNode head;
    private TaskNode tail;

    public void add(TaskNode taskNode) {
        synchronized (this) {
            if (null == head) {
                head = new TaskNode();
                tail = head;
                return;
            }

            tail.next = taskNode;
            taskNode.pre = tail;
            tail = tail.next;
        }
    }

    public void remove(TaskNode taskNode) {
        synchronized (this) {
            if (taskNode == head) {
                head = null;
                tail = null;
                return;
            }

            if (taskNode.getBucket() == this) {
                TaskNode pre = taskNode.pre;
                TaskNode next = taskNode.next;
                pre.next = next;
                next.pre = pre;
            }
        }
    }

    public void handleTask(Consumer<TaskNode> consumer) {
        TaskNode node = this.head;
        while (node != null) {
            remove(node);
            consumer.accept(node);
            node = node.next;
        }
        expireAt.set(-1);
    }

    public boolean setExpire(long expire) {
        return this.expireAt.getAndSet(expire) != expire;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, expireAt.longValue() - System.currentTimeMillis());
    }

    @Override
    public int compareTo(Delayed o) {
        return expireAt.intValue() - ((Bucket) o).expireAt.intValue();
    }
}
