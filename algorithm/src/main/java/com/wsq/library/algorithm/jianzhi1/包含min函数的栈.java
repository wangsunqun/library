package com.wsq.library.algorithm.jianzhi1;

import java.util.Deque;
import java.util.LinkedList;

public class 包含min函数的栈 {
    Deque<Integer> stackA = new LinkedList<>();
    Deque<Integer> stackB = new LinkedList<>();

    public void push(int x) {
        stackA.push(x);
        if (stackB.size() == 0 || stackB.peek() >= x) {
            stackB.push(x);
        }
    }

    public void pop() {
        if (stackA.pop().equals(stackB.peek())) {
            stackB.pop();
        }
    }

    public int top() {
        return stackA.peek();
    }

    public int min() {
        return stackB.peek();
    }
}
