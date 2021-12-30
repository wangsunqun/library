package com.wsq.library.algorithm.link;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wsq
 * @Description
 * @date 2021/12/30 10:33
 */
public class 环形链表 {
    public static boolean hasCycle(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        while (head != null) {
            if (set.contains(head)) {
                return true;
            }
            
            set.add(head);
            head = head.next;
        }
        
        return false;
    }

    public static void main(String[] args) {
        ListNode node3 = new ListNode(3, null);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        System.out.println(hasCycle(new ListNode(1, null)));
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }

        ListNode(int x, ListNode next) {
            val = x;
            this.next = next;
        }
    }
}
