package com.wsq.library.algorithm.jianzhi1;

import com.alibaba.fastjson.JSON;

import java.util.Deque;
import java.util.LinkedList;

public class 从尾到头打印链表 {
    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static int[] reversePrint(ListNode head) {
        Deque<Integer> stack = new LinkedList<>();

        while (head != null) {
            stack.push(head.val);
            head = head.next;
        }

        int[] data = new int[stack.size()];
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            data[i] = stack.pop();
        }

        return data;
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);

        System.out.println(JSON.toJSONString(reversePrint(node1)));
    }
}
