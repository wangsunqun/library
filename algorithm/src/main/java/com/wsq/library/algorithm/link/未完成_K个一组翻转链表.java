package com.wsq.library.algorithm.link;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class 未完成_K个一组翻转链表 {
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
        ListNode pre = new ListNode(0, head);
        ListNode last = pre;

        while (head != null) {
            ListNode tmp = head;
            ListNode end = getEnd(head, k);
            if (end == null) break;

            ListNode tmp2 = end.next;

            if (end.next != null) {
                ListNode listNode = reverseList(head, end);
                last.next = listNode;
                last = tmp;
                head = tmp2;
                head.next = tmp2;
            } else {
                last.next = tmp;
            }
        }

        return pre.next;
    }

    private static ListNode getEnd(ListNode head, int k) {
        while (head != null) {
            if (k == 0) {
                return head;
            }

            head = head.next;
            k--;
        }

        return head;
    }

    private static ListNode reverseList(ListNode head, ListNode end) {
        ListNode tmp = head;
        head = head.next;

        while (head != null) {
            ListNode tmp2 = head.next;
            head.next = tmp;
            tmp = head;
            head = tmp2;

            if (tmp == end) break;
        }

        return tmp;
    }

    public static void main(String[] args) {
        ListNode node3 = new ListNode(3, null);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        final ListNode listNode = reverseKGroup(node1, 2);
        System.out.println(JSON.toJSONString(listNode));
    }
}
