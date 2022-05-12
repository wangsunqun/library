package com.wsq.library.algorithm.jianzhi1;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class 队列的最大值 {
    private Queue<Integer> queue1 = new LinkedList<>();
    private Deque<Integer> queue2 = new LinkedList<>();

    public int max_value() {
        if (queue2.isEmpty()) return -1;
        return queue2.peek();
    }

    public void push_back(int value) {
        queue1.add(value);

        while (!queue2.isEmpty() && queue2.peekLast() < value) {
            queue2.pollLast();
        }

        queue2.addLast(value);
    }

    public int pop_front() {
        if (queue1.isEmpty()) return -1;

        Integer poll = queue1.poll();

        if (!queue2.isEmpty() && queue2.peek().equals(poll)) {
            queue2.pop();
        }

        return poll;
    }
}
