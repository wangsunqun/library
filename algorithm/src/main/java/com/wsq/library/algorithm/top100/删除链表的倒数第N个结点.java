package com.wsq.library.algorithm.top100;

public class 删除链表的倒数第N个结点 {
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        int count = 0;

        ListNode node = head;
        while (node != null) {
            node = node.next;
            count++;
        }

        count = count - n - 1;
        node = head;
        while (count > 0) {
            node = node.next;
            count--;
        }

        if (count == 0) {
            node.next = node.next.next;
        } else {
            head = node.next;
        }

        return head;
    }

    public static class ListNode {
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
}
