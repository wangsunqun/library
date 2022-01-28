package com.wsq.library.algorithm.jianzhi1;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 队列右为尾，左为头。作为栈时候push是从头压栈
 * @author wsq
 * @Description
 * @date 2022/1/28 10:08
 */
public class 用两个栈实现队列 {
    static Deque<Integer> stack1 = new LinkedList<>();
    static Deque<Integer> stack2 = new LinkedList<>();

    public static void appendTail(int value) {
        stack1.push(value);
    }

    public static int deleteHead() {
        if (!stack2.isEmpty()) return stack2.pop();

        if (stack1.isEmpty()) return -1;

        while (!stack1.isEmpty()) {
            stack2.push(stack1.pop());
        }

        return stack2.pop();
    }

    public static void main(String[] args) {
        System.out.println(deleteHead());
        appendTail(5);
        appendTail(2);
        appendTail(3);
        System.out.println(deleteHead());
        System.out.println(deleteHead());
        System.out.println(deleteHead());
    }
}
