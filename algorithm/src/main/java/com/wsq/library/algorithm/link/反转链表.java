package com.wsq.library.algorithm.link;

import com.alibaba.fastjson.JSON;

public class 反转链表 {
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

    public static ListNode reverseList(ListNode head) {
        ListNode tmp = null;
        while (head != null) {
            ListNode tmp2 = head.next;
            head.next = tmp;
            tmp = head;
            head = tmp2;
        }

        return tmp;
    }

    public static void main(String[] args) {
        ListNode node3 = new ListNode(3, null);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        final ListNode listNode = reverseList(node1);
        System.out.println(JSON.toJSONString(listNode));
    }
}
