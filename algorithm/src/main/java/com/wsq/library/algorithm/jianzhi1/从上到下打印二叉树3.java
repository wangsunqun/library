package com.wsq.library.algorithm.jianzhi1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class 从上到下打印二叉树3 {
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> deque = new LinkedList<>();
        deque.add(root);

        while (!deque.isEmpty()) {
            LinkedList<Integer> data = new LinkedList<>();

            for (int i = deque.size(); i > 0; i--) {
                TreeNode node = deque.poll();
                if ((res.size() & 1) == 0) {
                    data.addLast(node.val);
                } else {
                    data.addFirst(node.val);
                }

                if (node.left != null) deque.add(node.left);
                if (node.right != null) deque.add(node.right);
            }

            res.add(data);
        }

        return res;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);

        node1.left = node2;
        node1.right = node3;
        node2.left = node4;
        node3.right = node5;

        List<List<Integer>> lists = levelOrder(node1);

        System.out.println();
    }
}
