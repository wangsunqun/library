package com.wsq.library.algorithm.jianzhi1;


public class 反转链表 {
    public static ListNode reverseList(ListNode head) {
        ListNode before = null;
        ListNode after;

        while (head != null) {
            after = head.next;
            head.next = before;
            before = head;
            head = after;
        }

        return before;
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
//        n1.next = n2;
//        n2.next = n3;

        ListNode listNode = reverseList(n1);

        System.out.println();
    }
}
