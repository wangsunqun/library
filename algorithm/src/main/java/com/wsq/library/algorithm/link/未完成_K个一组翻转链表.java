package com.wsq.library.algorithm.link;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;

public class 未完成_K个一组翻转链表 {
    @Data
    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode pre = null;

        while (head != null) {
            ListNode last = head;
            ListNode end = getEnd(head, k);
            if (pre == null) pre = end;

            if (end == null) break;

            head = reverseList(head, end);
            last.next = head;
        }

        return pre;
    }

    private static ListNode getEnd(ListNode head, int k) {
        while (head != null) {
            if (--k == 0) {
                return head;
            }

            head = head.next;
        }

        return head;
    }

    private static ListNode reverseList(ListNode head, ListNode end) {
        ListNode tmp = null;

        while (head != null) {
            if (tmp == end) break;

            ListNode tmp2 = head.next;
            head.next = tmp;
            tmp = head;
            head = tmp2;
        }

        return head;
    }

    public static void main(String[] args) {
        ListNode node5 = new ListNode(5, null);
        ListNode node4 = new ListNode(4, node5);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        ListNode listNode = reverseKGroup(node1, 2);
        System.out.println(JSON.toJSONString(listNode));
    }
}
