package com.wsq.library.algorithm.stack;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author wsq
 * @Description
 * @date 2021/12/30 16:28
 */
public class 最小栈 {
    Deque<Integer> stack = new LinkedList<>();

    public void push(int val) {
        stack.push(val);
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return stack.stream().min(Comparator.comparing(Integer::intValue)).get();
    }
}
