package com.wsq.library.algorithm.jianzhi1;

public class 链表中倒数第k个节点 {
    public static ListNode getKthFromEnd(ListNode head, int k) {
        ListNode after = head;
        for (int i = 0; i < k - 1; i++) {
            after = after.next;
        }

        while (after.next != null) {
            head = head.next;
            after = after.next;
        }

        return head;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        n1.next = n2;
        n2.next = n3;

        ListNode listNode = getKthFromEnd(n1, 4);

        System.out.println();
    }
}
